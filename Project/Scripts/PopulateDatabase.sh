SOURCE_FILES_FOLDER=$1
HDFS_DATABASE_LOCATION=$2

hadoop fs -copyFromLocal $SOURCE_FILES_FOLDER/product.csv $HDFS_DATABASE_LOCATION/product

hadoop fs -copyFromLocal $SOURCE_FILES_FOLDER/store.csv $HDFS_DATABASE_LOCATION/store

hadoop fs -copyFromLocal $SOURCE_FILES_FOLDER/employee.csv $HDFS_DATABASE_LOCATION/employee

hadoop fs -copyFromLocal $SOURCE_FILES_FOLDER/loyalty.csv $HDFS_DATABASE_LOCATION/loyalty

hadoop fs -copyFromLocal $SOURCE_FILES_FOLDER/promotions.csv $HDFS_DATABASE_LOCATION/promotion