# -- > Create S3 Bucket
echo $(awslocal s3 mb s3://resource-bucket)
# --> List S3 Buckets
echo $(awslocal s3 ls)