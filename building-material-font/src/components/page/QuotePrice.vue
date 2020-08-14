<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="icon icondetail icon-lg-baojiaguanli"></i> 报价信息
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button
                        type="primary"
                        icon="el-icon-delete"
                        class="handle-del mr10"
                        @click="delAllSelection"
                >批量删除</el-button>

                <el-button
                        type="warning"
                        icon="el-icon-share"
                        class="handle-del mr10"
                        style="padding-left: 11px"
                        @click="handleAnalysis"
                >报价分析</el-button>

                <el-button
                        type="success"
                        icon="el-icon-download"
                        class="handle-del mr10"
                        @click="downloadTemp"
                >模板下载</el-button>

                <el-upload
                        class="upload-demo"
                        :show-file-list="false"
                        accept=".xlsx,.xls"
                        action
                        :before-upload="beforeUpload"
                        :on-exceeded-size="handleMaxSize"
                        :max-size="fileSize"
                        style="display: inline-block; padding-left: 11px"
                        list-type="picture"
                        :http-request="uploadRequest"
                        >
                    <el-button :disabled="importDataDisabled" type="success" :icon="importDataBtnIcon">{{importDataBtnText}}</el-button>
                </el-upload>


            </div>

            <div class="handle-box">
                <el-input v-model="query.materialName" placeholder="材料名称" class="handle-input mr10"></el-input>
                <el-input v-model="query.norm" placeholder="材料规格" class="handle-input mr10"></el-input>
                <el-input v-model="query.materialQuality" placeholder="材料材质" class="handle-input mr10"></el-input>
            </div>
            <div class="handle-box">
                <el-select v-model="query.supplierId" placeholder="经销商" class="handle-select mr10">
                    <el-option
                            v-for="item in supplierData"
                            :key="item.id"
                            :label="item.supplierName"
                            :value="item.id"
                    ></el-option>
                </el-select>
                <el-select v-model="query.state" placeholder="状态" class="handle-select mr10">
                    <el-option key="0" label="在售" value="0"></el-option>
                    <el-option key="1" label="下架" value="1"></el-option>
                </el-select>
                <el-input v-model="query.factoryName" placeholder="生产商" class="handle-input mr10"></el-input>
            </div>
            <div class="handle-box">

                <span class="spanInfo">发布日期范围</span>
                <el-date-picker v-model="query.releaseStartTime" class="handle-input mr10" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" ></el-date-picker>
                <span class="spanInfo">-</span>
                <el-date-picker v-model="query.releaseEndTime" class="handle-input mr10" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" ></el-date-picker>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                <el-button type="info" icon="el-icon-refresh" @click="doEmpty">清空</el-button>
            </div>



            <el-table
                    :data="quotePriceData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" width="55" align="center" prop="id"></el-table-column>
                <el-table-column prop="materialName" label="材料名称"></el-table-column>
                <el-table-column prop="norm" label="规格"></el-table-column>
                <el-table-column width="60" prop="materialQuality" label="材质"></el-table-column>
                <el-table-column width="70" prop="materialQuantity" label="起订量"></el-table-column>
                <el-table-column width="55" prop="materialUnit" label="单位"></el-table-column>
                <el-table-column prop="materialPrice" label="单价/元"></el-table-column>
                <el-table-column min-width="100px" prop="releaseDate" :formatter="dateFormat" label="发布日期"></el-table-column>
                <el-table-column label="状态">
                    <template scope="scope">
                        <el-tag type="success" v-if="scope.row.state === 0">在售</el-tag>
                        <el-tag type="danger"  v-else>下架</el-tag>
                    </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="supplierName" label="经销商"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="factoryName" label="生产商"></el-table-column>
                <el-table-column min-width="100px" prop="manufactureDate" :formatter="dateFormat" label="生产日期"></el-table-column>
                <el-table-column prop="qualityGuaranteePeriod" label="保质期"></el-table-column>
                <el-table-column prop="originPlace" label="产地"></el-table-column>
                <el-table-column label="操作" width="100" align="center">
                    <template slot-scope="scope">
                        <el-button
                                type="text"
                                icon="el-icon-delete"
                                class="red"
                                @click="handleDelete(scope.$index, scope.row)"
                        >删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                        background
                        layout="total, prev, pager, next"
                        :current-page="query.pageIndex"
                        :page-size="query.pageSize"
                        :total="pageTotal"
                        @current-change="handlePageChange"
                ></el-pagination>
            </div>
        </div>

        <el-dialog title="报价分析" :visible.sync="analysisVisible" width="30%">
            <el-form ref="searchForm" :rules="rules" label-width="80px" :model="searchForm">
                <el-form-item label="报价材料" prop="materialId">
                    <el-cascader
                            style="width: 100%"
                            clearable
                            ref="selectMaterialName"
                            placeholder="可输入材料名称搜索，例如：水泥"
                            :options="optionsSearch"
                            @change="analysisIt"
                            v-model="searchForm.materialId"
                            filterable></el-cascader>
                </el-form-item>
                <el-form-item label="时间范围" prop="dateRange">
                    <el-date-picker
                            v-model="searchForm.dateRange"
                            type="monthrange"
                            style="width: 100%"
                            range-separator="至"
                            value-format="yyyy-MM"
                            :picker-options="factEndDateFun"
                            start-placeholder="开始月份"
                            end-placeholder="结束月份">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="analysisVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitItAn">确 定</el-button>
            </span>
        </el-dialog>

        <el-dialog :title="chartLineText" :visible.sync="lineChartVisible" width="40%">
            <div id="chartLine" style="width:100%; height:400px;"></div>
        </el-dialog>

    </div>
</template>

<script>
    import { querySupplierAll } from '../../api/supplier';
    import { queryFactoryAll } from '../../api/factory';
    import { queryQuotePrice, delQuotePrice , downloadTempQuotePrice , uploadQuotePriceData } from '../../api/quotePrice'
    import moment from 'moment';
    import { analysisQuotePrice } from '../../api/quotePrice'
    import { searchMaterialAndCategory } from '../../api/materialStock'
    import echarts from 'echarts';
    export default {
        name: 'quotePriceTable',
        data() {
            return {
                factEndDateFun: {
                    disabledDate(time) {
                        return time.getTime() > Date.now() - 8.64e6;//如果没有后面的-8.64e6就是不可以选择今天的
                    }
                },
                query: {
                    norm: '',
                    materialQuality: '',
                    materialName: '',
                    releaseStartTime: '',
                    releaseEndTime: '',
                    state: '',
                    supplierId: '',
                    factoryName: '',
                    pageIndex: 1,
                    pageSize: 10
                },
                quotePriceData: [],
                multipleSelection: [],
                pageTotal: 0,
                idx: -1,
                id: -1,
                supplierData: [],
                factoryData: [],
                materialCategoryData: [],
                importDataBtnText: '报价导入',
                importDataBtnIcon: 'el-icon-upload',
                importDataDisabled: false,
                fileSize:10,
                analysisVisible: false,
                optionsSearch: [],
                searchForm: {},
                rules: {
                    materialId: [{ required: true, message: '请选择报价材料', trigger: 'blur' }],
                    dateRange: [{ required: true, message: '请选择时间范围', trigger: 'blur' }]
                },
                lineChartVisible: false,
                chartLine: null,
                chartLineText: '',
                xAxisList: [],
                seriesList: [],
                legendList: [],
            };
        },
        created() {
            this.querySupplier();
            this.queryFactory();
            this.getQuotePrice();

        },
        methods: {
            // 获取数据
            getQuotePrice() {
                queryQuotePrice(this.query).then(res => {
                    if (res.code === '1001') {
                        this.quotePriceData = res.data;
                        this.pageTotal = res.total;
                    } else if (res.code === '2001') {
                        this.$message.error(res.msg);
                    } else {//3001
                        this.$message.error(res.msg);
                        localStorage.removeItem('userName');
                        localStorage.removeItem('token');
                        this.$router.push('/login');
                    }
                });
            },
            handleAnalysis() {
                this.searchForm = {};
                this.searchMaterialCategory();
                this.analysisVisible = true;
            },
            analysisIt(value) {
                this.searchForm.materialId = value[1];
                this.chartLineText = this.$refs["selectMaterialName"].getCheckedNodes()[0].label+'价格走势图';
            },

            submitItAn() {
                /*this.xAxisList = [];
                this.seriesList = [];
                this.legendList = [];*/
                console.log(this.searchForm)
                this.$refs.searchForm.validate(valid => {
                    if (valid) {
                        analysisQuotePrice(this.searchForm).then(res => {
                            console.log(res);
                            if (res.code === '1001') {
                                this.lineChartVisible = true;//打开折线统计图弹出框
                                this.xAxisList = res.data.xAxisList;
                                this.seriesList = res.data.seriesList;
                                this.legendList = res.data.legendList;
                                setTimeout(() => {
                                    this.drawLineChart();
                                },300)
                            } else if (res.code === '2001') {
                                this.$message.error(res.msg);
                            } else {//3001
                                this.$message.error(res.msg);
                                localStorage.removeItem('userName');
                                localStorage.removeItem('token');
                                this.$router.push('/login');
                            }
                        })
                    }
                })
            },

            drawLineChart() {
                this.chartLine = echarts.init(document.getElementById('chartLine'));

                this.chartLine.setOption({
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: this.legendList
                    },
                    grid: {
                        left: '6%',
                        right: '6%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: this.xAxisList
                    },
                    yAxis: {
                        type: 'value',
                        name: "单价/￥"
                    },
                    series: this.seriesList
                }, true);
            },
            // 触发搜索按钮
            handleSearch() {
                this.$set(this.query, 'pageIndex', 1);
                this.getQuotePrice();
            },

            // 删除操作
            handleDelete(index, row) {
                // 二次确认删除
                this.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    var del = [];
                    del.push(row.id);
                    delQuotePrice(del).then(res => {
                        if (res.code === "1001") {
                            this.$message.success(res.msg);
                            this.multipleSelection = [];
                            this.getQuotePrice();
                        } else if (res.code === '2001') {
                            this.$message.error(res.msg);
                        } else {//3001
                            this.$message.error(res.msg);
                            localStorage.removeItem('userName');
                            localStorage.removeItem('token');
                            this.$router.push('/login');
                        }
                    });
                }).catch(() => {});
            },

            // 多选操作
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            // 多选删除
            delAllSelection() {
                const length = this.multipleSelection.length;
                if (length === 0) {
                    this.$message.warning('请至少选择一行数据');
                } else {
                    var delArray = [];
                    for (let i = 0; i < length; i++) {
                        delArray.push(this.multipleSelection[i].id);
                    }

                    this.$confirm('确定要删除吗？', '提示', {
                        type: 'warning'
                    }).then(() => {
                        delQuotePrice(delArray).then(res => {
                            if (res.code === "1001") {
                                this.$message.success(res.msg);
                                this.multipleSelection = [];
                                this.getQuotePrice();
                            } else if (res.code === '2001') {
                                this.$message.error(res.msg);
                            } else {//3001
                                this.$message.error(res.msg);
                                localStorage.removeItem('userName');
                                localStorage.removeItem('token');
                                this.$router.push('/login');
                            }
                        });
                    }).catch(() => {});
                }
            },

            // 上传未成功前状态
            beforeUpload() {
                this.importDataBtnText = "正在导入";
                this.importDataBtnIcon = "el-icon-loading";
                this.importDataDisabled = true;
            },

            uploadRequest(item) {
                //var multiparty = require('multiparty');
                const fileObj = item.file;
                const data = new FormData();
                data.append('file', fileObj);
                uploadQuotePriceData(data).then (res => {
                    if (res.code === '1001') {
                        this.$message.success("导入成功");
                    } else {
                        //this.$message.error(res.msg);
                        this.$message({
                            dangerouslyUseHTMLString: true,
                            message: res.msg,
                            type: 'error'
                        });
                    }
                    this.getQuotePrice();
                    this.afterUpload();
                }).catch(error => {
                    this.$message.error("导入失败");
                });
            },
            afterUpload() {
              this.importDataBtnIcon = "el-icon-upload";
              this.importDataBtnText = "报价导入";
              this.importDataDisabled = false;
            },

            handleMaxSize (file) {
                this.$message.error("文件大小不得超过10M");
            },
            // 正在导入状态
            beforeUpload() {
                this.importDataBtnText = "正在导入";
                this.importDataBtnIcon = "el-icon-loading";
                this.importDataDisabled = true;
            },
            // 模板下载
            downloadTemp() {
                downloadTempQuotePrice().then(res => {
                    let url = window.URL.createObjectURL(new Blob([res]));
                    let link = document.createElement("a");
                    link.style.display = "none";
                    link.href = url;
                    link.setAttribute("download", "报价信息模板.xlsx");
                    document.body.appendChild(link);
                    link.click();
                }).catch(error => {
                    this.$message.error("下载模板失败");
                });
            },



            // 分页导航
            handlePageChange(val) {
                this.$set(this.query, 'pageIndex', val);
                this.getQuotePrice();
            },

            // 清空说说看
            doEmpty() {
                this.query = {
                    'norm': '',
                    'materialQuality': '',
                    'materialName': '',
                    'releaseStartTime': '',
                    'releaseEndTime': '',
                    'state': '',
                    'supplierId': '',
                    'factoryName': '',
                    "pageIndex": 1,
                    "pageSize": 10
                };
            },

            searchMaterialCategory() {
                searchMaterialAndCategory().then(res => {
                    if (res.code === '1001') {
                        this.optionsSearch = res.data;
                    } else if (res.code === '2001') {
                        this.$message.error(res.msg);
                    } else {//3001
                        this.$message.error(res.msg);
                        localStorage.removeItem('userName');
                        localStorage.removeItem('token');
                        this.$router.push('/login');
                    }
                });
            },
            // 获取供应商数据
            querySupplier() {
                querySupplierAll().then(res => {
                    if (res.code === '1001') {
                        this.supplierData = res.data;
                    } else if (res.code === '2001') {
                        this.$message.error(res.msg);
                    } else {//3001
                        this.$message.error(res.msg);
                        localStorage.removeItem('userName');
                        localStorage.removeItem('token');
                        this.$router.push('/login');
                    }
                });
            },
            // 获取厂家数据
            queryFactory() {
                queryFactoryAll().then(res => {
                    if (res.code === '1001') {
                        this.factoryData = res.data;
                    } else if (res.code === '2001') {
                        this.$message.error(res.msg);
                    } else {//3001
                        this.$message.error(res.msg);
                        localStorage.removeItem('userName');
                        localStorage.removeItem('token');
                        this.$router.push('/login');
                    }
                });
            },


            // 时间格式胡
            dateFormat:function(row, column) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD");
            },
        }
    };
</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 200px;
    }
    .handle-select1 {
        width: 100%;
    }
    .handle-input {
        width: 200px;
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
