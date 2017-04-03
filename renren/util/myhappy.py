import happybase as hb
class HbaseHelp:
	def __init__(self):
		try:
			self.connection=hb.Connection("192.168.1.114")
			self.connection.open()
			self.table=self.connection.table("renrenInfo")
		except:
			error="hbase连接出现问题....."
	def __del__(self):
		pass
		# if self.connection is not None:
		# 	self.connection.close()
	def getrows(self,row_start,row_end):
		rows=self.table.scan(row_start=row_start,row_stop=row_end)
		#print(help(rows))
		f=open("calculateCount.txt",'w')
		i=0
		while True:
			try:
				k=rows.__next__()
			except StopIteration:
				f.close()
				break
			i+=1
			f.write(k[0].decode()+"\n")
			#print(k[0])
		print(i)
	def scanAllData(self):
		i=0
		for key, data in self.table.scan():
			i+=1
		print("allcount:%d"%i)
	def close(self):
		try:
			self.connection.close()
		except:
			print("hbase close error")
if __name__=='__main__':
	hb=HbaseHelp()
	#hb.getrows('502800001','502899999')
	hb.scanAllData()