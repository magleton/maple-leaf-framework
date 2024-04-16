package cn.maple.core.framework.code;

import cn.hutool.http.HttpStatus;

public enum GXDefaultResultStatusCode implements GXResultStatusCode {
    OK() {
        @Override
        public int getCode() {
            return HttpStatus.HTTP_OK;
        }

        @Override
        public String getMsg() {
            return "操作成功";
        }
    },
    SMS_SEND_SUCCESS() {
        @Override
        public int getCode() {
            return HttpStatus.HTTP_OK;
        }

        @Override
        public String getMsg() {
            return "短信发送成功";
        }
    },
    SMS_SEND_FAILURE() {
        @Override
        public int getCode() {
            return HttpStatus.HTTP_OK;
        }

        @Override
        public String getMsg() {
            return "短信发送失败";
        }
    },
    INTERNAL_SYSTEM_ERROR() {
        @Override
        public int getCode() {
            return HttpStatus.HTTP_INTERNAL_ERROR;
        }

        @Override
        public String getMsg() {
            return "内部系统错误";
        }
    },
    INPUT_TOO_SHORT() {
        @Override
        public int getCode() {
            return 1100;
        }

        @Override
        public String getMsg() {
            return "输入为空,或者输入字数不够!";
        }
    },
    DATA_NOT_FOUND() {
        @Override
        public int getCode() {
            return 1200;
        }

        @Override
        public String getMsg() {
            return "数据对象不存在";
        }
    },
    SMS_CAPTCHA_ERROR() {
        @Override
        public int getCode() {
            return 1300;
        }

        @Override
        public String getMsg() {
            return "短信验证码错误";
        }
    },
    GRAPH_CAPTCHA_ERROR() {
        @Override
        public int getCode() {
            return 1400;
        }

        @Override
        public String getMsg() {
            return "图形验证码错误";
        }
    },
    WRONG_PHONE() {
        @Override
        public int getCode() {
            return 1500;
        }

        @Override
        public String getMsg() {
            return "手机号有误";
        }
    },
    NEED_PERMISSION() {
        @Override
        public int getCode() {
            return 1600;
        }

        @Override
        public String getMsg() {
            return "权限不足";
        }
    },
    LOGIN_ERROR() {
        @Override
        public int getCode() {
            return 1700;
        }

        @Override
        public String getMsg() {
            return "登录错误,账号或者密码错误!";
        }
    },
    STATUS_ERROR() {
        @Override
        public int getCode() {
            return 1800;
        }

        @Override
        public String getMsg() {
            return "当前状态不正确";
        }
    },
    NEED_GRAPH_CAPTCHA() {
        @Override
        public int getCode() {
            return 1900;
        }

        @Override
        public String getMsg() {
            return "请传递图形验证码";
        }
    },
    USER_NAME_EXIST() {
        @Override
        public int getCode() {
            return 2000;
        }

        @Override
        public String getMsg() {
            return "用户名已存在";
        }
    },
    PARAMETER_VALIDATION_ERROR() {
        @Override
        public int getCode() {
            return 2100;
        }

        @Override
        public String getMsg() {
            return "参数验证错误";
        }
    },
    TOKEN_TIMEOUT_EXIT() {
        @Override
        public int getCode() {
            return 2200;
        }

        @Override
        public String getMsg() {
            return "登录信息已过期,请重新登录";
        }
    },
    FILE_ERROR() {
        @Override
        public int getCode() {
            return 2300;
        }

        @Override
        public String getMsg() {
            return "不正确的文件格式";
        }
    },
    PARSE_REQUEST_JSON_ERROR() {
        @Override
        public int getCode() {
            return 2400;
        }

        @Override
        public String getMsg() {
            return "解析请求的JSON参数出错";
        }
    },
    REQUEST_JSON_NOT_BODY() {
        @Override
        public int getCode() {
            return 2500;
        }

        @Override
        public String getMsg() {
            return "请求的JSON参数出错为空";
        }
    },
    ACCOUNT_FREEZE() {
        @Override
        public int getCode() {
            return 2600;
        }

        @Override
        public String getMsg() {
            return "账号被冻结";
        }
    },
    USER_NOT_EXISTS() {
        @Override
        public int getCode() {
            return 2700;
        }

        @Override
        public String getMsg() {
            return "用户不存在";
        }
    }
}
