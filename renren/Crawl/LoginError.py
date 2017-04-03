    #自定义异常类
    class Loginerror(RuntimeError):
        def __init__(self, arg):
            self.args = arg