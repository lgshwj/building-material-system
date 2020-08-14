<template>
    <div class="header">
        <!-- 折叠按钮 -->
        <div class="collapse-btn" @click="collapseChage">
            <i v-if="!collapse" class="el-icon-s-fold"></i>
            <i v-else class="el-icon-s-unfold"></i>
        </div>
        <div class="logo">后台管理系统</div>
        <div class="header-right">
            <div class="header-user-con">
                <!-- 全屏显示 -->
                <div class="btn-fullscreen" @click="handleFullScreen">
                    <el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
                        <i class="el-icon-rank"></i>
                    </el-tooltip>
                </div>
                <!-- 消息中心 -->
                <!--<div class="btn-bell">
                    <el-tooltip
                        effect="dark"
                        :content="message?`有${message}条未读消息`:`消息中心`"
                        placement="bottom"
                    >
                        <router-link to="/tabs">
                            <i class="el-icon-bell"></i>
                        </router-link>
                    </el-tooltip>
                    <span class="btn-bell-badge" v-if="message"></span>
                </div>-->
                <!-- 用户头像 -->
                <div class="user-avator">
                    <img src="../../assets/img/img.jpg" />
                </div>
                <!-- 用户名下拉菜单 -->
                <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{username}}
                        <i class="el-icon-caret-bottom"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <!--<a href="https://github.com/lin-xin/vue-manage-system" target="_blank">
                            <el-dropdown-item>项目仓库</el-dropdown-item>
                        </a>-->
                        <el-dropdown-item command="updatePassword">修改密码</el-dropdown-item>
                        <el-dropdown-item divided command="loginout">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>

        <el-dialog title="修改密码" :visible.sync="updateVisible" width="30%">
            <el-form ref="passwordForm" :rules="rules" :model="passwordForm" label-width="80px">
                <el-form-item label="旧密码" prop="oldPassword">
                    <el-input type="password" v-model="passwordForm.oldPassword"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                    <el-input type="password" v-model="passwordForm.newPassword"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="updateVisible = false">取 消</el-button>
                <el-button type="primary" @click="savePassword">确 定</el-button>
            </span>
        </el-dialog>

    </div>
</template>
<script>
import bus from '../common/bus';
import { updatePassword } from '../../api/login'
export default {
    data() {
        return {
            collapse: false,
            fullscreen: false,
            name: 'steel',
            message: 2,
            updateVisible: false,
            rules: {
                oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
                newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
            },
            passwordForm: {},
        };
    },
    computed: {
        username() {
            let username = localStorage.getItem('userName');
            return username ? username : this.name;
        }
    },

    methods: {
        // 用户名下拉菜单选择事件
        handleCommand(command) {
            if (command === 'loginout') {
                this.loginoutIt();
            } else if (command === 'updatePassword') {
                this.passwordForm = {};
                this.updateVisible = true;
            }
        },
        loginoutIt() {
            this.$confirm('操作将退出系统, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$message({
                    type: 'success',
                    message: '退出系统成功'
                });
                localStorage.removeItem('userName');
                localStorage.removeItem('token');
                this.$router.push('/login');
            }).catch(() => {
                this.$message({
                    type: 'warning',
                    message: '已取消退出系统'
                });
            });

        },
        savePassword() {
            this.$refs.passwordForm.validate(valid => {
                if (valid) {
                    this.passwordForm.token = localStorage.getItem("token");
                    updatePassword(this.passwordForm).then(res => {
                        if(res.code === '1001') {
                            this.updateVisible = false;
                            this.$message.success(res.msg);
                            localStorage.removeItem('userName');
                            localStorage.removeItem('token');
                            this.$router.push('/login');
                        } else if(res.code === '2001'){
                            this.$message.error(res.msg);
                        } else {
                            // 3001信息
                            this.$message.error(res.msg);
                            this.loginoutIt();
                        }
                    });
                } else {
                    this.$message.error('修改失败');
                }
            });
        },
        // 侧边栏折叠
        collapseChage() {
            this.collapse = !this.collapse;
            bus.$emit('collapse', this.collapse);
        },
        // 全屏事件
        handleFullScreen() {
            let element = document.documentElement;
            if (this.fullscreen) {
                if (document.exitFullscreen) {
                    document.exitFullscreen();
                } else if (document.webkitCancelFullScreen) {
                    document.webkitCancelFullScreen();
                } else if (document.mozCancelFullScreen) {
                    document.mozCancelFullScreen();
                } else if (document.msExitFullscreen) {
                    document.msExitFullscreen();
                }
            } else {
                if (element.requestFullscreen) {
                    element.requestFullscreen();
                } else if (element.webkitRequestFullScreen) {
                    element.webkitRequestFullScreen();
                } else if (element.mozRequestFullScreen) {
                    element.mozRequestFullScreen();
                } else if (element.msRequestFullscreen) {
                    // IE11
                    element.msRequestFullscreen();
                }
            }
            this.fullscreen = !this.fullscreen;
        }
    },
    mounted() {
        if (document.body.clientWidth < 1500) {
            this.collapseChage();
        }
    }
};
</script>
<style scoped>
.header {
    position: relative;
    box-sizing: border-box;
    width: 100%;
    height: 70px;
    font-size: 22px;
    color: #fff;
}
.collapse-btn {
    float: left;
    padding: 0 21px;
    cursor: pointer;
    line-height: 70px;
}
.header .logo {
    float: left;
    width: 250px;
    line-height: 70px;
}
.header-right {
    float: right;
    padding-right: 50px;
}
.header-user-con {
    display: flex;
    height: 70px;
    align-items: center;
}
.btn-fullscreen {
    transform: rotate(45deg);
    margin-right: 5px;
    font-size: 24px;
}
.btn-bell,
.btn-fullscreen {
    position: relative;
    width: 30px;
    height: 30px;
    text-align: center;
    border-radius: 15px;
    cursor: pointer;
}
.btn-bell-badge {
    position: absolute;
    right: 0;
    top: -2px;
    width: 8px;
    height: 8px;
    border-radius: 4px;
    background: #f56c6c;
    color: #fff;
}
.btn-bell .el-icon-bell {
    color: #fff;
}
.user-name {
    margin-left: 10px;
}
.user-avator {
    margin-left: 20px;
}
.user-avator img {
    display: block;
    width: 40px;
    height: 40px;
    border-radius: 50%;
}
.el-dropdown-link {
    color: #fff;
    cursor: pointer;
}
.el-dropdown-menu__item {
    text-align: center;
}
</style>
