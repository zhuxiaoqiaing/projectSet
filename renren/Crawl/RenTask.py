import os
import os.path
import time
import sys
sys.path.append("..")
from MobileRenRen import MobileRenRen
from server.RenRenSocket import *
import datetime
import random
import config
from util import util
import json
from copy import deepcopy
class RenRenTask:
    def __init__(self):
        self.taskfile=config.joblogFileName
        #初始作业为未完成状态
        self.TaskFail=True
        self.currentjob=config.currentjob
        self.oldjob=config.oldjob
        self.statusformat="*******************************\n\n"
        self.final_fail_status=":爬虫程序异常崩溃,生成Taskfail标识.....\n"+self.statusformat
        self.final_success_status=":爬虫程序完成抓取任务,生成Tasksuccess标识.......\n"+self.statusformat
        """
        the task information
        """
        self.taskfail=config.taskfailFile
        self.tasksuccess=config.tasksuccessFile
        self.moveCompletedJob2oldJobs()
        self.jobs=util.loadFormJson(self.currentjob)
        self.taskInformation=time.strftime("%Y-%m-%d %H:%M:%S")+":爬虫程序开始启动......\n"
        self.writeTaskInformation(self.taskInformation)
        self.checkTaskStatus()
    def moveCompletedJob2oldJobs(self):
        finishedidJob={}
        currentJob={}
        newJob={}
        if os.path.exists(self.oldjob):
            finishedidJob=util.loadFormJson(self.oldjob)
        if os.path.exists(self.currentjob):
            currentJob=util.loadFormJson(self.currentjob)
        newJob=deepcopy(currentJob)
        for id in currentJob:
            value=int(currentJob[id])
            if value>=99999:
                del newJob[id]
                finishedidJob[id]=value
        util.dumpToJson(finishedidJob,self.oldjob)
        util.dumpToJson(newJob,self.currentjob)
        newJob.clear()
        currentJob.clear()
        finishedidJob.clear()
    def writeTaskInformation(self,taskInformation):
        with open(self.taskfile,"a") as f:
                f.write(taskInformation)
    def checkTaskStatus(self):
        if os.path.exists(self.taskfail):
            os.remove(self.taskfail)
        if os.path.exists(self.tasksuccess):
            print("抓取任务已经完成,即将退出....")
            self.taskInformation="抓取任务已经完成,即将退出...."
            self.taskInformation+=time.strftime("%Y-%m-%d %H:%M:%S")+self.final_success_status
            self.writeTaskInformation(self.taskInformation)
            exit()
        if len(self.jobs)<=0:
            print("不存在jobs文件.....,即将退出")
            self.taskInformation="不存在jobs,即将退出...."
            self.taskInformation+=time.strftime("%Y-%m-%d %H:%M:%S")+self.final_fail_status
            self.writeTaskInformation(self.taskInformation)
            exit()
    def writeTaskStatus(self):
        currentendtime=time.strftime("%Y-%m-%d %H:%M:%S")
        if not self.TaskFail:
            if not os.path.exists(self.tasksuccess):
                print("抓取任务完成,生成Tasksuccess标识.......")
                self.taskInformation=currentendtime+self.final_success_status
                os.mknod(self.tasksuccess)
           #任务自动执行失败，则算失败。。
        if self.TaskFail:
            if not os.path.exists(self.taskfail):
                print("抓取任务中断,生成Taskfail标识.....")
                self.taskInformation=currentendtime+self.final_fail_status
                os.mknod(self.taskfail)        
        print("将任务执行信息写入日志中......")
        self.writeTaskInformation(self.taskInformation)
    def runningTask(self):
        #start time
        starttime = datetime.datetime.now()
        renren=MobileRenRen()
        renren.renren_login()
        #whether allow to open server that provide imformation of task for client
        if config.openServer:
            renrenserver=RenRenServer(renren)
            try:
                renrenserver.setDaemon(True)
                renrenserver.start()
            except:
                print("socket服务启动失败...")
                if not os.path.exists(self.taskfail):
                    os.mknod(self.taskfail)
                exit()
        
        #sort the jobs
        joblist=sorted(self.jobs.items(),key=lambda x:x[1],reverse=True)
        try:
            for jobtuple in joblist:
                currentkey=jobtuple[0]
                value=int(jobtuple[1])
                if value>=99999:
                    continue
                while value<=99999 and not renren.endProgram:
                	#http://3g.renren.com/profile.do
                    #将不足5位数的数字字符凑成5位数字字符,不足的高用0填充
                    strvalue=util.translateDigitTo5char(value)
                    urlprefix = "http://3g.renren.com/details.do?id=%s%s" % (currentkey,strvalue)
                    if not renren.endProgram:
                        renren.parse_userInformation(urlprefix)
                        value+=1
                else:
                    self.jobs[currentkey]=value
                    util.dumpToJson(self.jobs,self.currentjob)
                if renren.endProgram:
                    break
            else:
                self.TaskFail=False
        except Exception as e:
            print("程序结束的原因是:%s"%e)
            print("当前执行到的number是%d"%value)
            print("程序运行结束了.........")
            self.taskInformation="程序异常结束的原因是:%s"%e+"\n"
            self.writeTaskInformation(self.taskInformation)
            """
            the file represent the program fail
            """
        finally:
            self.jobs[currentkey]=value+1
            util.dumpToJson(self.jobs,self.currentjob)
            if renren.pagecount>=1:
            	self.taskInformation="成功抓取页面数:%d"%renren.pagecount+"\n"
            self.writeTaskInformation(self.taskInformation)
            self.writeTaskStatus()
            try:
                renren.hu.close()
                #renren.renrenf.close()
            except Exception as e:
                print(e)
                print("closing the database connection occur error")
            for speed in renren.speedlist:
                print("speed:%s"%speed)
            endtime = datetime.datetime.now()
            allsecond = (endtime - starttime).seconds
            print("程序执行时间为%d 秒" % allsecond)
#calculate the sleep time
sleepTime=5
def monitorTask():
    global sleepTime
    try:
        task=RenRenTask()
        task.runningTask()
    except Exception as e:
        print("task fail%s"%e)
    finally:
        while True:
            if os.path.exists(config.taskfailFile):
                try:
                    prompt_relax="%s:休息%d分钟再来检测.......\n"%((time.strftime("%Y-%m-%d %H:%M:%S"),sleepTime))
                    print(prompt_relax)
                    task.writeTaskInformation(prompt_relax)
                    time.sleep(60*sleepTime)
                    print("任务重启.........")
                    if os.path.exists("Taskfail"):
                        os.remove("Taskfail")
                    sleepTime*=2
                    if sleepTime>60*2:
                        sleepTime=10
                    monitorTask()
                except Exception as e:
                    prompt_error="%s监控过程出现问题........%s"%(time.strftime("%Y-%m-%d %H:%M:%S"),e)
                    print(prompt_error)
            else:
                if os.path.exists(config.tasksuccessFile):
                    print("任务成功完成...")
                    print("检测程序退出...")
                    exit()
if __name__=="__main__":
    monitorTask()