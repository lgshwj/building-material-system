<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-share"></i> 采购统计
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-select @change="chooseStatisticType" v-model="type" placeholder="统计方式"  class="handle-select mr10">
                    <el-option key="1" label="月" value="1"></el-option>
                    <el-option key="2" label="年" value="2"></el-option>
                </el-select>

                <el-date-picker
                        @change="chooseStatisticStartDate"
                        :value-format="formatType"
                        :picker-options="factEndDateFunStart"
                        v-model="startDate"
                        class="dateSelect"
                        :type="dateType"
                        placeholder="开始时间"
                        :disabled="disabledStart">
                </el-date-picker>
                <span class="spanInfo">-</span>
                <el-date-picker
                        @change="chooseStatisticEndDate"
                        :picker-options="factEndDateFunEnd"
                        v-model="endDate"
                        class="dateSelect"
                        :type="dateType"
                        :value-format="formatType"
                        placeholder="结束时间"
                        :disabled="disabledEnd">
                </el-date-picker>
                <el-button :disabled="btStatus" type="primary" icon="el-icon-search" @click="handSearchStatstic">统计</el-button>
            </div>
            <section class="chart-container" style="padding-top: 20px">
                <el-row>
                    <el-col :span="24" >
                        <div id="chartBarCategory" style="width:100%; height:400px;"></div>
                    </el-col>
                </el-row>
                <el-row style="margin-top: 30px">
                    <el-col :span="24">
                        <div id="chartBarSupplier" style="width:100%; height:400px;"></div>
                    </el-col>
                </el-row>
            </section>
        </div>

    </div>
</template>

<script>
    import echarts from 'echarts';
    import {  statisticBySelect } from '../../api/statistic';

    export default {
        name: 'statisticPurchaseTable',

        data() {
            return {
                btStatus: true,
                chartBarCategory: null,
                chartBarSupplier: null,
                comeBackData: {},
                query: {},
                dateType: 'year',
                formatType: 'yyyy',
                type: '',
                startDate: '',
                endDate: '',
                disabledStart: true,
                disabledEnd: true,
                factEndDateFunStart: {
                    disabledDate(time) {
                        return time.getTime() > Date.now();// 不能大于当前时间
                    }
                },
                factEndDateFunEnd: this.settingEndDateRange(),
            };
        },
        created() {
            //this.getPurchaseStatistic();
        },
        methods: {

            settingEndDateRange() {
                const self = this;
                return {
                    disabledDate(time) {
                        //设置结束时间不能小于开始且不能大于当前时间
                        return time.getTime() > Date.now() || time.getTime() <= new Date(self.startDate).getTime();
                    }
                }
            },

            chooseStatisticType:function(e){
                if (e === '1') {
                    this.dateType = 'month';
                    this.formatType = 'yyyy-MM';
                    this.disabledStart = false;
                }
                if (e === '2') {
                    this.dateType = 'year';
                    this.formatType = 'yyyy';
                    this.disabledStart = false;
                }
            },
            chooseStatisticStartDate: function(e) {
                this.disabledEnd = false;
            },
            chooseStatisticEndDate: function(e) {
                this.btStatus = false;
            },
            handSearchStatstic() {
                if (this.type === null || this.type === '') {
                    this.$message.error('请选择统计方式');
                } else if (this.startDate === null  || this.startDate === '') {
                    this.$message.error('请选择开始时间');
                } else if (this.endDate === null || this.endDate === ''){
                    this.$message.error('请选择结束时间');
                } else {
                    let data = {"type": this.type, "startDate": this.startDate, "endDate": this.endDate};
                    this.getStatisticBySelect(data);
                }
            },
            getStatisticBySelect(data) {
                statisticBySelect(data).then(res => {
                    if (res.code === '1001') {
                        this.comeBackData = res.data;
                        this.drawBarChartCategory();
                        this.drawBarChartSupplier();
                        this.$message.success(res.msg);
                    } else if (res.code === '2001'){
                        this.$message.error(res.msg);
                    } else if (res.code === '4001') {
                        this.comeBackData = res.data;
                        this.drawBarChartCategory();
                        this.drawBarChartSupplier();
                        this.$message.warning(res.msg);
                    }
                })
            },

            drawBarChartCategory() {
                this.chartBar = echarts.init(document.getElementById('chartBarCategory'));
                this.chartBar.setOption({
                    title: {
                        text: '采购材料类别统计'
                    },
                    color: ['#003366', '#006699', '#4cabce', '#e5323e'],
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    legend: {
                        data: this.comeBackData.legendCategory
                    },
                    xAxis: [
                        {
                            type: 'category',
                            axisTick: {show: false},
                            data: this.comeBackData.xAxisList
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: this.comeBackData.barChartCategorySeries
                });
            },
            drawBarChartSupplier() {
                this.chartBar = echarts.init(document.getElementById('chartBarSupplier'));
                this.chartBar.setOption({
                    title: {
                        text: '经销商供货量统计'
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    legend: {
                        data: this.comeBackData.legendSupplier
                    },
                    xAxis: [
                        {
                            type: 'category',
                            axisTick: {show: false},
                            data: this.comeBackData.xAxisList
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: this.comeBackData.barChartSupplierSeries
                });
            },

        }
    };
</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 190px;
    }

    .handle-input {
        width: 190px;
        display: inline-block;
    }
    .table {
        width: 100%;
        font-size: 14px;
    }
    .red {
        color: #ff0000;
    }
    .mr10 {
        margin-right: 10px;
    }
    .dateSelect {
        margin-right: 10px;
        width: 190px;
    }
    .table-td-thumb {
        display: block;
        margin: auto;
        width: 40px;
        height: 40px;
    }
    .spanInfo {
        width: 80px;
        line-height: 32px;
        font-size: 14px;
        color: #606266;
        padding: 0 12px 0 0
    }
</style>
