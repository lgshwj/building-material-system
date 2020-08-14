<template>
    <div>
        <el-row :gutter="20" class="mgb20">
            <el-col :span="8">
                <el-card shadow="hover" :body-style="{padding: '0px'}">
                    <div class="grid-content grid-con-1">
                        <i class="el-icon-lx-recharge grid-con-icon"></i>
                        <div class="grid-cont-right">
                            <div class="grid-num">{{purchaseTotalMoney}}</div>
                            <div>采购总金额（万元）</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="hover" :body-style="{padding: '0px'}">
                    <div class="grid-content grid-con-3">
                        <i class="el-icon-lx-goods grid-con-icon"></i>
                        <div class="grid-cont-right">
                            <div class="grid-num">{{purchaseTotalQuantity}}</div>
                            <div>采购总数量（单位）</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="hover" :body-style="{padding: '0px'}">
                    <div class="grid-content grid-con-2">
                        <i class="el-icon-lx-rank grid-con-icon"></i>
                        <div class="grid-cont-right">
                            <div class="grid-num">{{useTotalQuantity}}</div>
                            <div>消耗总数量（单位）</div>
                        </div>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 60px">
            <el-col :span="12">
                <div id="chartBarSupplierQuantityByYear" style="width:100%; height:400px;"></div>
            </el-col>
            <el-col :span="12">
                <div id="chartLineTotalMoneyByYear" style="width:100%; height:400px;"></div>
            </el-col>
        </el-row>
    </div>



</template>

<script>
    import echarts from 'echarts';
    import { statisticPurchase } from '../../api/statistic';
    export default {
        name: 'dashboard',
        data() {
            return {
                chartBarSupplierQuantityByYear: null,
                chartLineTotalMoneyByYear: null,
                comeBackData: {},
                purchaseTotalQuantity: 0,
                purchaseTotalMoney: 0,
                useTotalQuantity: 0,
            };
        },
        created() {
            this.getPurchaseStatistic();
        },

        methods: {
            getPurchaseStatistic() {
                statisticPurchase().then(res => {
                    this.comeBackData = res.data;
                    this.purchaseTotalQuantity = res.data.purchaseTotalQuantity;
                    this.purchaseTotalMoney = res.data.purchaseTotalMoney;
                    this.useTotalQuantity = res.data.useTotalQuantity;
                    this.drawBarChartSupplierQuantityEveryYear();
                    this.drawLineChartDealMoneyEveryYear();
                })
            },
            drawBarChartSupplierQuantityEveryYear() {
                this.chartBar = echarts.init(document.getElementById('chartBarSupplierQuantityByYear'));
                this.chartBar.setOption({
                    title: {
                        text: '每年经销商总供货量'
                    },
                    color: ['#003366', '#006699', '#4cabce', '#e5323e'],
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    legend: {
                        data: this.comeBackData.legendList
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
                            type: 'value',
                            name: '材料单位'
                        }
                    ],
                    series: this.comeBackData.barSeriesList
                });
            },
            drawLineChartDealMoneyEveryYear() {
                this.chartLine = echarts.init(document.getElementById('chartLineTotalMoneyByYear'));
                this.chartLine.setOption({
                    title: {
                        text: '每年所花总金额走势'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: this.comeBackData.legendList
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: this.comeBackData.xAxisList
                    },
                    yAxis: {
                        type: 'value',
                        name: "万元"
                    },
                    series: this.comeBackData.lineSeriesList
                });
            },

        }
    };
</script>


<style scoped>
    .grid-content {
        display: flex;
        align-items: center;
        height: 100px;
    }

    .grid-cont-right {
        flex: 1;
        text-align: center;
        font-size: 14px;
        color: #999;
    }

    .grid-num {
        font-size: 30px;
        font-weight: bold;
    }

    .grid-con-icon {
        font-size: 50px;
        width: 100px;
        height: 100px;
        text-align: center;
        line-height: 100px;
        color: #fff;
    }

    .grid-con-1 .grid-con-icon {
        background: rgb(45, 140, 240);
    }

    .grid-con-1 .grid-num {
        color: rgb(45, 140, 240);
    }

    .grid-con-2 .grid-con-icon {
        background: rgb(100, 213, 114);
    }

    .grid-con-2 .grid-num {
        color: rgb(45, 140, 240);
    }

    .grid-con-3 .grid-con-icon {
        background: rgb(242, 94, 67);
    }

    .grid-con-3 .grid-num {
        color: rgb(242, 94, 67);
    }

    .user-info-cont div:first-child {
        font-size: 30px;
        color: #222;
    }

    .user-info-list span {
        margin-left: 70px;
    }

    .mgb20 {
        margin-top: 20px;
        margin-bottom: 20px;
    }
</style>
