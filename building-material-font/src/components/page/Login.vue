<template>
    <div class="login-wrap">

        <div class="slideShadow" v-show="showSlide">
            <transition>
                <div class="slideSty animated bounce">
                    <slide-verify
                            @success="onSuccess"
                            @fail="onFail"
                            @again="onAgain"
                            :sliderText="text"
                            :w="350"
                            :h="175"
                            ref="slideDiv"
                            :imgs="imgs"
                    ></slide-verify>
                    <div class="iconBtn">
                        <i class="el-icon-circle-close" @click="closeCheck"></i
                        ><i class="el-icon-refresh" @click="refresh"></i>
                    </div>
                </div>
            </transition>
        </div>

        <div class="ms-login">
            <div class="ms-title">后台管理系统</div>
            <el-form :model="param" :rules="rules" ref="login" label-width="0px" class="ms-content">
                <el-form-item prop="username">
                    <el-input tabindex="1" v-model="param.userName" placeholder="账号">
                        <el-button slot="prepend" icon="el-icon-lx-people"></el-button>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input
                        tabindex="2"
                        type="password"
                        placeholder="密码"
                        v-model="param.password"
                        @keyup.enter.native="submitForm()"
                    >
                        <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                    </el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button tabindex="2" type="primary" @click="submitForm()">登录</el-button>
                </div>
                <p class="login-tips">Tips : 输入账号和密码</p>
            </el-form>
        </div>
    </div>
</template>

<script>

import { login } from '../../api/login';
import Vue from 'vue';
import SlideVerify from 'vue-monoplasty-slide-verify';
Vue.use(SlideVerify);
import img0 from '../../assets/slide_images/slide-0.jpg';
import img1 from '../../assets/slide_images/slide-1.jpg';
import img2 from '../../assets/slide_images/slide-2.jpg';
import img3 from '../../assets/slide_images/slide-3.jpg';
import img4 from '../../assets/slide_images/slide-4.jpg';
export default {
    data: function() {
        return {
            notifyObj: null,
            text: '向右滑动',
            showSlide: false,
            imgs: [
                img0,
                img1,
                img2,
                img3,
                img4,
            ],
            param: {
                userName: '',
                password: '',
            },
            rules: {
                userName: [{ required: true, message: '请输入账号', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
            },
        };
    },
    methods: {
        closeCheck() {
            this.refresh();
            this.showSlide = false;
        },
        refresh() {
            this.$refs.slideDiv.reset()
        },
        onSuccess() {
            this.showSlide = false;
            this.loginIt();
        },
        onFail() {
            this.refresh();
            this.$message.error('验证失败')
        },
        onAgain() {
            this.text = '请重新向右滑动';
            this.refresh();
        },
        submitForm() {
            this.$refs.login.validate(valid => {
                if (valid) {
                    this.showSlide = true;
                } else {
                    this.$message.error('请输入账号和密码');
                    return false;
                }
            });
        },
        loginIt() {
            login(this.param.userName,this.param.password).then(res => {
                if (res.code === '1001') {
                    this.$message.success(res.msg);
                    localStorage.setItem('token', res.data.token);
                    localStorage.setItem('userName', res.data.username);
                    this.$router.push('/');
                } else {
                    this.$message.error(res.msg);
                    this.refresh();
                }
            });
        },
    },
};
</script>

<style scoped>
.login-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    background-image: url(../../assets/img/login-bg.jpg);
    background-size: 100%;
}
.ms-title {
    width: 100%;
    line-height: 50px;
    text-align: center;
    font-size: 20px;
    color: #fff;
    border-bottom: 1px solid #ddd;
}
.ms-login {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 350px;
    margin: -190px 0 0 -175px;
    border-radius: 5px;
    background: rgba(255, 255, 255, 0.3);
    overflow: hidden;
}
.ms-content {
    padding: 30px 30px;
}
.login-btn {
    text-align: center;
}
.login-btn button {
    width: 100%;
    height: 36px;
    margin-bottom: 10px;
}
.login-tips {
    font-size: 12px;
    line-height: 30px;
    color: #fff;
}
.slideShadow {
    position: fixed;
    z-index: 999;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6);
}
.slideSty {
    position: absolute;
    width: 380px;
    height: 311px;
    background: #e8e8e8;
    border: 1px solid #dcdcdc;
    left: 50%;
    top: 50%;
    margin-left: -188px;
    margin-top: -176px;
    z-index: 99;
    border-radius: 5px;
    font-size: 12px;
}
.iconBtn {
    padding: 9px 0 0 19px;
    color: #5f5f5f;
    border-top: 1px solid #d8d8d8;
    margin-top: 17px;
}
.iconBtn>i {
    font-size: 20px;
    cursor: pointer;
}
.iconBtn>i:last-child {
    margin-left: 7px;
}
</style>
<style>
    .slideSty .slide-verify {
        margin: 13px auto 0 auto;
        width: 350px !important;
    }
    .slideSty .slide-verify-slider {
        width: 100% !important;
    }
    .slideSty .slide-verify-refresh-icon {
        display: none;
    }
</style>
