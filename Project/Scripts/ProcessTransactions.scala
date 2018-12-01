//val transactionFile = sc.textFile("/data/Project/Transactions/trans_log.csv");
val transactionFile = sc.textFile(sc.getConf.get("spark.driver.args"));
val targetDir = "/data/Project/Output.txt";

val records = transactionFile.map(x => x.split(','))

case class Product(transactionID: Int, productCode: String, amount: Double, discount: Double, addRemove: Int, storeNum: String, employeeNum: String, lane: Int, date: String)

val dfProduct = records.filter(x => x(1) == "TT").map(q => Product( q(0).toInt, q(3), q(4).toDouble, q(5).toDouble, q(6).toInt, q(7), q(8), q(9).toInt, q(10) )).toDF()

case class ProductLoyalty(transactionID: Int, loyaltyCode: String)

val dfLoyalty = records.filter(x => x(1) == "LL").map(q => ProductLoyalty( q(0).toInt, q(2))).toDF()

sqlContext.sql("USE project")

dfProduct.registerTempTable("DFProduct");
dfLoyalty.registerTempTable("DFLoyalty");

sqlContext.sql("SELECT * FROM DFProduct")

/* sqlContext.sql("SELECT a.transactionID, b.product_id, c.store_id, e.loyalty_id AS LoyaltyID, amount, discount, date FROM DFProduct a INNER JOIN product b ON (a.productCode = b.product_code) INNER JOIN store c ON (c.store_num = a.storeNum) LEFT JOIN DFLoyalty d ON (d.transactionID = a.transactionID) LEFT JOIN loyalty e ON (e.card_no = d.loyaltyCode)").show() */

var mdfOutput = sqlContext.sql("SELECT a.TransactionID, b.product_id, c.store_id, e.loyalty_id AS LoyaltyID, amount, discount, date AS TransactionDate FROM DFProduct a INNER JOIN product b ON (a.productCode = b.product_code) INNER JOIN store c ON (c.store_num = a.storeNum) LEFT JOIN DFLoyalty d ON (d.transactionID = a.transactionID) LEFT JOIN loyalty e ON (e.card_no = d.loyaltyCode)")

//mdfOutput = mdfOutput.withColumn("TransactionID",monotonicallyIncreasingId)

mdfOutput.registerTempTable("Output")

sqlContext.sql("set hive.exec.dynamic.partition.mode=nonstrict")

def getTransId(ts:String, d_store_id:Int, d_product:Int, d_trans_seq:Int): Long = {
   val daydate = ts.replaceAll("-","").split(" ")(0)
   val store_id = "%05d".format(d_store_id)
   val product = "%02d".format(d_product)
   val trans_seq = "%04d".format(d_trans_seq)

   (daydate + store_id.toString + product.toString + trans_seq.toString).toLong
}

val transId = getTransId(_,_,_,_)

sqlContext.udf.register("getTransId", transId)

sqlContext.sql("TRUNCATE TABLE transaction_stage")

sqlContext.sql("INSERT INTO transaction_stage SELECT getTransId(TransactionDate, store_id, product_id, TransactionID) AS TransactionID, product_id, store_id, LoyaltyID, amount, discount, CAST(TransactionDate AS DATE)  FROM Output")

sqlContext.sql("INSERT INTO transaction PARTITION(transaction_date) SELECT TransactionID, product_id, store_id, loyalty_id, amount, discount, transaction_date  FROM transaction_stage WHERE TransactionID NOT IN (SELECT TransactionID FROM Transaction)");

// val prepSaveFile = mdfOutput.map(x => x.mkString("|"))

// prepSaveFile.saveAsTextFile(targetDir)


/*
SELECT a.transactionID, b.product_id, c.store_id, e.loyalty_id AS LoyaltyID, amount, discount, date AS TransactionDate
FROM DFProduct a
INNER JOIN product b ON (a.productCode = b.product_code)
INNER JOIN store c ON (c.store_num = a.storeNum)
LEFT JOIN DFLoyalty d ON (d.transactionID = a.transactionID)
LEFT JOIN loyalty e ON (e.card_no = d.loyaltyCode)
*/

