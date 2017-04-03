import sys
sys.path.append("..")
import happybase as hb
import config
import pymysql
import urllib
import pickle
"""
hbase db operate
"""
class HbaseHelp:
	def __init__(self):
		try:
			self.connection=hb.Connection(config.hbase_masterHost)
			self.connection.open()
			self.table=self.connection.table(config.hbase_tablename)
		except:
			error="hbase连接出现问题....."
	def insertRow(self,row,columnvalueDict):
		rowbytes=row.encode(encoding="utf-8")
		columnDictbytes={}
		for column in columnvalueDict:
			columnbyte=column.encode(encoding='utf-8')
			valuebyte=columnvalueDict[column].encode(encoding='utf-8')
			columnDictbytes[columnbyte]=valuebyte
		self.table.put(rowbytes,columnDictbytes)
	def __del__(self):
		pass
		# if self.connection is not None:
		# 	self.connection.close()
	def scanAllData(self):
		for key, data in self.table.scan():
			print(key.decode(),data)
	def close(self):
		try:
			self.connection.close()
		except:
			print("hbase close error")
"""
mysql db operator
"""
class pysqlHelp:
    def __init__(self):
        self.hostname=config.mysql_port
        self.port=config.mysql_port
        self.user=config.mysql_user
        self.passwd=config.mysql_password
        self.db=config.mysql_db
        self.table=config.mysql_table
    def get_connection(self):
        try:
            conn = pymysql.connect(host=self.hostname,port=self.port,user=self.user,passwd=self.passwd,db=self.db,charset='utf8')
            print("连接mysql server 成功")
            return conn
        except Exception as e:
            print("连接mysql server出现异常")
            print(e)
    def create_table(self,con):
        mycuror=con.cursor()
        try:
            mycuror.execute("""
                    CREATE TABLE `RenRenInfo` (  
					  `id` NOT NULL,  
					  `username` varchar(20), 
					   `head` varchar(100),
					  `sex` varchar(20), 
					  `birthday`  varchar(50),  
					  `address` varchar(50),  
					  `lastLogin`  varchar(60),  
					  PRIMARY KEY (`id`)  
					) DEFAULT CHARSET=utf8; 
                            """)
            con.commit()
        except:
            print("创建表失败")
        finally:
            mycuror.close()
            con.close()
    def insert(self,con,id,username,head,sex,birthday,address,lastLogin):
        mycuror = con.cursor()
        insert_sql=u"INSERT INTO RenRenInfo (id,username,head,sex,birthday,address,lastLogin) VALUES('%s','%s','%s','%s','%s','%s','%s')" %(id,username,head,sex,birthday,address,lastLogin)
        try:
            mycuror.execute(insert_sql)
            con.commit()
            #print("插入成功")
        except Exception as e:
            print("插入失败")
            print(e)
        finally:
            mycuror.close()
            #con.close()
if __name__=='__main__':
	#hbase test
	hu=HbaseHelp()
	hu.insertRow('xqhadoop',{"baseInfo:age":'12'})
