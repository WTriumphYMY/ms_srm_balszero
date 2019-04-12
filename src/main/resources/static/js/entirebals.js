var result;

$(function(){

    $('#entirebals').addClass("active");

    $('#calBtn').bind('click',function(){
        submitForm()
    });

    //setInputValue()
    $('#srmName').blur(function(){
        var srmName = $('#srmName').val();
        $.ajax({
            url:"/module/bals/blur",
            type:"post",
            dataType:"json",
            data:"srmName="+srmName,
            async:false,
            success:function(data){
                setInputValue(data);
            }
        });

    });

    $('#resultBtn').click(function(){
        $('#myModal').modal('toggle');
        drawChart(result.t, result.pb, '压强');
    });

    $('#pbBtn').click(function(){
        drawChart(result.t, result.pb, '压强');
    });

    $('#fBtn').click(function(){
        drawChart(result.t, result.F, '推力');
    });

    $('#mrBtn').click(function(){
        drawChart(result.t, result.qt, '质量流率');
    });

    $('#tempBtn').click(function(){
        drawChart(result.t, result.temperature, '燃烧室温度');
    });

    $('#dtBtn').click(function(){
        drawChart(result.t, result.dt, '喉径');
    });
})

function submitForm(){
    var form = new FormData($("#calForm")[0]);
    $.ajax({
        url:"/module/bals",
        type:"post",
        data:form,
        cache: false,
        processData: false,
        contentType: false,
        success:function(rtn){
            if(rtn.success){
                alert(rtn.message);
                //成功的话，显示结果按钮可按
                $('#resultBtn').removeAttr('disabled');
                setData(rtn);
            }else{
                alert(rtn.message);
            }
        },
        error:function(e){
            alert("网络错误，请重试！！");
        }
    });
}

function setInputValue(data){
    $('#a_main').val(data.propmain.a);
    $('#n_main').val(data.propmain.n);
    $('#dens_main').val(data.propmain.dens);
    $('#cs_main').val(data.propmain.cstar);
    $('#tp_main').val(data.propmain.tp);
    $('#rg_main').val(data.propmain.rg);
    $('#k_main').val(data.propmain.k);
    $('#tb_main').val(data.propmain.tburn);

    $('#a_ig').val(data.propig.a);
    $('#n_ig').val(data.propig.n);
    $('#dens_ig').val(data.propig.dens);
    $('#cs_ig').val(data.propig.cstar);
    $('#tp_ig').val(data.propig.tp);
    $('#rg_ig').val(data.propig.rg);
    $('#k_ig').val(data.propig.k);

    $('#dt').val(data.motor.dt);
    $('#ek').val(data.motor.ek);
    $('#ekTime').val(data.motor.ekTime);
    $('#epsA').val(data.motor.epsA);
    $('#vc').val(data.motor.vc0);
    $('#popen').val(data.motor.pOpen);
    $('#xq').val(data.motor.xq);
    $('#coeig').val(data.motor.coeIg);
    $('#dtig').val(data.motor.dtig);

    $('#ra').val(data.env.ra);
    $('#pa').val(data.env.p0);
    $('#ta').val(data.env.temp0);
    $('#tw').val(data.env.tempw);

   // $('#step').val(data.env.envH);

   // $('#a_main').val(0.00265);
   // $('#n_main').val(0.4);
   // $('#dens_main').val(1790.0);
   // $('#cs_main').val(1603.3);
   // $('#tp_main').val(3768.0);
   // $('#rg_main').val(434.235);
   // $('#k_main').val(1.16);
   // $('#tb_main').val(800);
   // $('#a_ig').val(0.01351);
   // $('#n_ig').val(0.4);
   // $('#dens_ig').val(1780.0);
   // $('#cs_ig').val(1567.45);
   // $('#tp_ig').val(3768);
   // $('#rg_ig').val(434.235);
   // $('#k_ig').val(1.16);
   // $('#dt').val(0.1);
   // $('#ek').val(0.0003);
   // $('#ekTime').val(0.0);
   // $('#epsA').val(30);
   // $('#vc').val(0.035);
   // $('#popen').val(2000000);
   // $('#xq').val(0.99);
   // $('#coeig').val(0.2);
   // $('#dtig').val(0.028);
   // $('#ra').val(286.846);
   // $('#pa').val(101325.0);
   // $('#ta').val(298);
   // $('#tw').val(298);
   // $('#step').val(0.001);
}

function setData(data){
    result = data;
}

function drawChart(xdata, ydata, yname){
    var chart = Highcharts.chart('container', {
        title: {
            text: '内弹道'
        },
        boost: {
            useGPUTranslations: true
        },
        subtitle: {
            text: '数据来源：我算出来的'
        },
        xAxis: {
            categories: xdata
        },
        yAxis: {
            title: {
                text: yname
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle'
        },

        series: [{
            name: yname,
            data: ydata
        }],
        responsive: {
            rules: [{
                condition: {
                    maxWidth: 500
                },
                chartOptions: {
                    legend: {
                        layout: 'horizontal',
                        align: 'center',
                        verticalAlign: 'bottom'
                    }
                }
            }]
        }
    });
}