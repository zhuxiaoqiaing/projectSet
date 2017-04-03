#hase config
hbase_masterHost='192.168.1.114'
hbase_tablename="renrenInfo"


#mysql config
mysql_host="localhost"
mysql_port=3306
mysql_user="admin"
mysql_password="admin"
mysql_db="state"
mysql_table="RenRenInfo"

#job information
joblogFileName="../jobInfo/Taskinfo.log"
currentjob="../jobInfo/job.json"
oldjob="../jobInfo/oldjob.json"
taskfailFile="../jobInfo/Taskfail"
tasksuccessFile="../jobInfo/Tasksuccess"
""" the server whether allow client to connect to it,
	and get the task progress
"""
openServer=False
server_port="../server/port.txt"



#user account for logining renren
headers=["Mozilla/5.0 (Windows NT 5.1; rv:33.0) Gecko/20100101 Firefox/33.0",
		"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Maxthon 2.0)",
		"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0",
		"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
		"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)",
		"Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5",
		"MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
		"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)",
		"Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1",
		"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0",		
		"Mozilla/4.0 (compatible; MSIE 6.0; ) Opera/UCWEB7.0.2.37/28/999",
]
userlist=[('554084963@163.com','wu2114801'),('GUANJILUN2007@163.com','dian460'),('1806320505@qq.com','zxx19921228'),("aq986477533@163.com","wu3128461"),
                    ('zhizhaopv83@163.com','yan756501'),('fdangykan89@163.com','qin271586'),
                    ('bestsheep522@163.com','ya5560488'),('418159617@163.com','jian35856'),
                    ]
limitSpeed=False
#the intervals of visitting the page
limitSpeedSize=0.2