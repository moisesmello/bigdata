SOURCE_FILES_FOLDER="/data/Project"
HDFS_DATABASE_LOCATION="/data/Project/Database"
DATABASE_NAME="project"

hadoop fs -mkdir -p $HDFS_DATABASE_LOCATION
hadoop fs -chmod -R 777 $HDFS_DATABASE_LOCATION

impala-shell -q "CREATE DATABASE IF NOT EXISTS $DATABASE_NAME LOCATION '$HDFS_DATABASE_LOCATION';"

hive -v --hiveconf dbname=$DATABASE_NAME -f CreateTables.hql

sh -x PopulateDatabase.sh $SOURCE_FILES_FOLDER $HDFS_DATABASE_LOCATION