import sys
sys.path.append("..")
import socket
import threading
import random
import config
from MobileRenRen import *
class conthread(threading.Thread):
    def __init__(self,conn,renren):
        threading.Thread.__init__(self)
        self.conn=conn
        self.renren=renren
    def run(self):
        is_exit=False
        while not is_exit:
            try:
                data=self.conn.recv(1024).decode()
                while not data:
                    data=self.conn.recv(1024).decode()
                print("接收到客户端的数据是:%s"%data)
                if data== '1':
                    resp=self.renren.getProcess()
                    print("resp:%s"%resp)
                    self.conn.sendall(resp.encode())
                if data=='0':
                    print('即将关闭客户端')
                    self.conn.close()
                    is_exit=True
                if data=='-1':
                    #结束程序运行
                    self.renren.endProgram=True
                    self.renren.endSign()
                    is_exit = True
            except Exception as e:
                print("关闭客户端异常的原因是%s"%e)
                is_exit = True
        if is_exit:
            print('服务器关闭客户端')
            try:
                self.conn.close()
            except:
                print("服务器关闭客户端出现问题")
class RenRenServer(threading.Thread):
    def __init__(self,renren):
        threading.Thread.__init__(self)
        self.HOST = ''
        self.PORT =random.choice(range(9900,10022))
        with open(config.server_port,"w") as f:
            f.write(str(self.PORT))
        self.renren=renren
        self.mysocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        try:
            self.mysocket.bind((self.HOST, self.PORT))
            print("启动服务器中.......")
        except Exception as e:
            print(e)
            print("bind error")
    #处理数据的处理器
    def run(self):
        self.mysocket.listen(10)
        while 1 and not self.renren.endProgram:
            if self.renren.endProgram:
                break
            conn, addr = self.mysocket.accept()
            print('Connected with ' + addr[0] + ':' + str(addr[1]))
            conthread(conn,self.renren).start()
            if self.renren.endProgram:
                break
        self.mysocket.close()
if __name__=='__main__':
    pass

