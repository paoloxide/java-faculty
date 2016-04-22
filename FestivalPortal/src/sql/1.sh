#! /bin/bash
# Get academy key
git clone ssh://jenkins@10.0.0.198:29418/chef_project
cp /var/lib/jenkins/jobs/Sprint5Test/workspace/FestivalPortal_Sprint5PGSolution/chef_project/academy_key.pem /var/lib/jenkins/jobs/Sprint5Test/workspace/FestivalPortal_Sprint5PGSolution
rm -rf /var/lib/jenkins/jobs/Sprint5Test/workspace/FestivalPortal_Sprint5PGSolution/chef_project
chmod 400 academy_key.pem


git clone https://r.thirumalai.prakash:Project123@innersource.accenture.com/tcfjava/tcfjava.git

ssh -i academy_key.pem -o StrictHostKeyChecking=no -tt root@10.0.2.5 "mkdir -p /testsql"
scp -i academy_key.pem -o StrictHostKeyChecking=no /var/lib/jenkins/jobs/Sprint5Test/workspace/FestivalPortal_Sprint5PGSolution/sql/* root@10.0.2.5:/testsql/
rm -rf /var/lib/jenkins/jobs/Sprint5Test/workspace/FestivalPortal_Sprint5PGSolution/tcfjava

ssh -i academy_key.pem -o StrictHostKeyChecking=no -tt root@10.0.2.5 "mysql --user=root --password='' < /testsql/FestivalDatabase_R1.sql"
ssh -i academy_key.pem -o StrictHostKeyChecking=no -tt root@10.0.2.5 "mysql --user=root --password='' < /testsql/FestivalDatabase_R1_TableData.sql"

ssh -i academy_key.pem -o StrictHostKeyChecking=no -tt root@10.0.2.5 "sudo rm -rf /var/lib/tomcat/webapps/*"
scp -i academy_key.pem -o StrictHostKeyChecking=no /var/lib/jenkins/jobs/Sprint5Test/workspace/FestivalPortal_Sprint5PGSolution/target/FestivalPortal_Sprint5PGSolution-0.0.1-SNAPSHOT.war root@10.0.2.5:/var/lib/tomcat/webapps
ssh -i academy_key.pem -o StrictHostKeyChecking=no -tt root@10.0.2.5 "sudo /etc/init.d/tomcat restart"
 
echo "Making sure the application is accessible by counting to 5..."
for i in 1 2 3 4 5; 
do 
echo $i; 
if [ $(curl -s -o /dev/null -w '%{http_code}' http://john.smith:Password01@10.0.1.5/FestivalPortal_Sprint5PGSolution/) -eq 200 ]  ; then 
break 
elif [ $i -eq 5 ]; 
then 
   echo "Application not running, exiting";
   exit 1; 
else 
   sleep 60; 
fi; 
done;

echo "Application deployed: http://$(curl http://169.254.169.254/latest/meta-data/public-ipv4)/petclinic"