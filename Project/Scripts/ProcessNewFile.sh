SOURCE_FOLDER="/home/Moises/Projct/data/NewFiles"
TARGET_FOLDER="/data/Project/Transactions/Input"
OUTPUT_FOLDER="/data/Project/Transactions/Output"

hadoop fs -copyFromLocal $SOURCE_FOLDER $TARGET_FOLDER

spark-shell -i ProcessTransactions.scala --conf spark.driver.args="$TARGET_FOLDER/trans_log.csv"