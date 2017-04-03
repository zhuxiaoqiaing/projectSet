from socket import *
import os
import os.path
import config
HOST = '127.0.0.1'
PORT = 13444
if os.path.exists(config.server_port):
    with open(config.server_port,"r") as f:
        PORT=int(f.readline().strip())
BUFSIZE = 1024
ADDR = (HOST,PORT)
try:
    tcpCliSock =socket(AF_INET, SOCK_STREAM)
    tcpCliSock.connect(ADDR)
except:
    print("服务器未启动,客户端即将退出...")
    exit()
orderdict={'-1':'结束爬虫抓取','0':'退出客户端','1':'爬虫运行进度'}
command='命令如下(只需输入对应命令的序号即可)\n-1,结束爬虫抓取\n0,退出客户端\n1,爬虫进度\n'
is_exit=False
while not is_exit:
    senddata = input('> (输入为空时结束)%s'%command)
    if senddata in ['\n','\r\n','End','']:
        senddata='0'
    if senddata in ['-1','0','1']:
        tcpCliSock.send(senddata.encode())
        replydata = tcpCliSock.recv(BUFSIZE).decode()
        if replydata!="":
            print("----------------------------------%s--------------------------------\n%s"%(orderdict[senddata],replydata))
    if senddata in ['0','-1']:
        print("客户端即将关闭")
        is_exit=True
print("tcp close")
tcpCliSock.close()
