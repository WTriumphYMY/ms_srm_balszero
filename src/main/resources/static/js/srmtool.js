$(function(){
    $('#pcBtn').popover(
        {
            trigger:'click', //触发方式
            placement: 'right',
            title:"弹出框标题",//设置 弹出框 的标题
            html: true, // 为true的话，data-content里就能放html代码了
            content:getContent()//这里可以直接写字符串，也可以 是一个函数，该函数返回一个字符串；
        }
    );

    $('#pcBtn').click(function(){
        $(this).popover('show');
    })
})

function getContent(){
    return '<div class="card-body">\n' +
        '            <form id="calForm" method="post" enctype="multipart/form-data">\n' +
        '                <div class="form-group">\n' +
        '                    <label for="srmName">发动机名称</label>\n' +
        '                    <input type="text" class="form-control" id="srmName" name="srmName" placeholder="你的发动机叫啥">\n' +
        '                </div>\n' +
        '                <small class="text-muted">主装药参数</small>\n' +
        '                <hr>\n' +
        '                <div class="form-group">\n' +
        '                    <label for="a_main">装药燃速系数</label>\n' +
        '                    <input type="text" class="form-control" id="a_main" name="propmain.a" placeholder="主装药燃速系数(MPa下m/s)">\n' +
        '                </div>\n' +
        '                <div class="form-group">\n' +
        '                    <label for="n_main">装药燃速压力指数</label>\n' +
        '                    <input type="text" class="form-control" id="n_main" name="propmain.n" placeholder="主装药压力指数(MPa下m/s)">\n' +
        '                </div>\n' +
        '                <div class="form-group">\n' +
        '                    <label for="dens_main">装药密度</label>\n' +
        '                    <input type="text" class="form-control" id="dens_main" name="propmain.dens" placeholder="主装药密度(kg/m^3)">\n' +
        '                </div>\n' +
        '                <div class="form-group">\n' +
        '                    <label for="cs_main">装药特征速度</label>\n' +
        '                    <input type="text" class="form-control" id="cs_main" name="propmain.cstar" placeholder="主装药特征速度(m/s)">\n' +
        '                </div>\n' +
        '                <div class="form-group">\n' +
        '                    <label for="tp_main">装药燃气温度</label>\n' +
        '                    <input type="text" class="form-control" id="tp_main" name="propmain.Tp" placeholder="主装药燃气温度(K)">\n' +
        '                </div>\n' +
        '                <div class="form-group">\n' +
        '                    <label for="rg_main">装药燃气气体常数</label>\n' +
        '                    <input type="text" class="form-control" id="rg_main" name="propmain.Rg" placeholder="主装药燃气气体常数(J/kg·K)">\n' +
        '                </div>\n' +
        '                <div class="form-group">\n' +
        '                    <label for="k_main">装药燃气比热比</label>\n' +
        '                    <input type="text" class="form-control" id="k_main" name="propmain.k" placeholder="主装药燃气比热比">\n' +
        '                </div>\n' +
        '                <div class="form-group">\n' +
        '                    <label for="tb_main">装药发火点温度</label>\n' +
        '                    <input type="text" class="form-control" id="tb_main" name="propmain.Tburn" placeholder="主装药起火点(K)">\n' +
        '                </div>\n' +
        '            </form>\n' +
        '        </div>\n' +
        '    </div>'
}