val transactionFile = sc.textFile("/data/Project/trans_log.csv");

val records = transactionFile.map(x => x.split(','))

case class Product(transactionID: Int, productCode: String, amount: Double, discount: Double, addRemove: Int, storeNum: String, employeeNum: String, lane: Int, date: String)

val dfProduct = records.filter(x => x(1) == "TT").map(q => Product( q(0).toInt, q(3), q(4).toDouble, q(5).toDouble, q(6).toInt, q(7), q(8), q(9).toInt, q(10) )).toDF()

case class ProductLoyalty(transactionID: Int, loyaltyCode: String)

val dfLoyalty = records.filter(x => x(1) == "LL").map(q => ProductLoyalty( q(0).toInt, q(2))).toDF()

sqlContext.sql("USE project")

dfProduct.registerTempTable("DFProduct");
dfLoyalty.registerTempTable("DFLoyalty");

sqlContext.sql("SELECT * FROM DFProduct")

sqlContext.sql("SELECT a.transactionID, b.product_id, c.store_id, e.loyalty_id AS LoyaltyID, amount, discount, date FROM DFProduct a INNER JOIN product b ON (a.productCode = b.product_code) INNER JOIN store c ON (c.store_num = a.storeNum) LEFT JOIN DFLoyalty d ON (d.transactionID = a.transactionID) LEFT JOIN loyalty e ON (e.card_no = d.loyaltyCode)").show()

/*
SELECT a.transactionID, b.product_id, c.store_id, e.loyalty_id AS LoyaltyID, amount, discount, date
FROM DFProduct a
INNER JOIN product b ON (a.productCode = b.product_code)
INNER JOIN store c ON (c.store_num = a.storeNum)
LEFT JOIN DFLoyalty d ON (d.transactionID = a.transactionID)
LEFT JOIN loyalty e ON (e.card_no = d.loyaltyCode)
*/

