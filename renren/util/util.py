import urllib
import json
import os
import os.path
"""
get the ip proxy from the internet
"""
def get_iplist():
    url = "http://www.youdaili.net/Daili/http/4713.html"
    req = urllib.request.Request(url)
    req.add_header('User-Agent', 'Mozilla/5.0')
    page = urllib.request.urlopen(req)
    html = page.read().decode('utf-8')
    # ip地址抽取
    # 抽取出的ip地址形如86.100.118.44:81@HTTP#美国
    p = r'(?:(?:[0-1]?\d?\d|2[0-4]\d|25[0-5])\.){3}(?:[0-1]?\d?\d|2[0-4]\d|25[0-5]):\d+@HTTP#(?:[\u4e00-\u9fa5]+)'
    iplist = re.findall(p, html)
    # 过滤掉国外的ip地址,防止访问过慢.影响爬虫速度
    ipfilterlist = []
    province = ['北京市', '天津市', '上海市', '重庆市', '河北省', '山西省', '辽宁省',
                '江苏省', '浙江省', '安徽省', '福建省', '江西省',
                '山东省', '河南省', '湖北省', '湖南省', '广东省', '海南省', '四川省']
    ports = ['80', '81', '82', '83', '8080']
    for each in iplist:
        for pro in province:
            if each.find(pro) != -1:
                port = each[each.find(":") + 1:each.find("@")]
                # 端口号过滤
                if port in ports:
                    # print(port)
                    ipfilterlist.append(each.split("@")[0])
    # for p in ipfilterlist:
    #     print(p)
    return ipfilterlist
def executeShell(command):
    os.system(command)  
"""
load from json
"""
def loadFormJson(filename):
    if not os.path.exists(filename):
        print("plese make sure that the job.json exists")
        return {}
    else:
        with open(filename,'r') as f:
            return json.load(f)
"""
write to json
"""
import os.path
def dumpToJson(data,filename):
    with open(filename,"w") as f:
        json.dump(data,f)
"""
将不足5位数的数字字符凑成5位数,不足的高用0填充
"""
zeaoSizeDict={0:"",1:'0',2:"00",3:'000',4:'0000'}

def translateDigitTo5char(intdigit):
    str_intdigit=str(intdigit)
    zeaoSize=5-len(str_intdigit)
    return zeaoSizeDict[zeaoSize]+str_intdigit
"""
读取文件startrow-endrows行数的所有userid前4位,返回id列表
"""
def loadPossibleId(filename,startrows,endrows):
	idlist=[]
	count=0
	if not os.path.exists(filename):
		print("该id文件不存在.....")
		#return idlist
	with open(filename,"r") as f:
		for line in f:
			count+=1
			if count>=startrows and count<=endrows:
				idlist.append(line.split("\t")[1].strip())
		return idlist
if __name__=="__main__":
	idlist=loadPossibleId("possibleId.txt",1,200)
	if idlist==None:
		print(" empty")
		exit()
	else:
		print("id is not null%d"%len(idlist))
		idDict={}
		for id in idlist:
			idDict[id]=1
		with open("job2.json","w") as f:
			json.dump(idDict,f)
