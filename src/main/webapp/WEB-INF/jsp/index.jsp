<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html style="height: 100%">
<head>
    <meta charset="utf-8">
    <title>商品销售额实时展示</title>
</head>
<body style="height: 100%; margin: 0">
<div id="container" style="height: 50%; width: 50%;left: 30%;top: 30%"></div>
<div id="message"></div>
<script src="/js/jquery-3.6.1.js"></script>
<script src="/js/echarts.min.js"></script>
<%--如果jq和echarts都没生效，可用下面的外链，记得注释掉上面那俩--%>
<%--<script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.6.1/jquery.min.js"></script>--%>
<%--<script src="https://cdn.bootcdn.net/ajax/libs/echarts/5.2.0/echarts.min.js"></script>--%>
<script type="text/javascript">
    var myChart = echarts.init(document.getElementById('container'));
    myChart.setOption({
        title:{
            text: '商品销售额汇总',
            subtext: '数据来自模拟'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer:{
                type: 'shadow'
            }
        },
        legend: {
            data:['2023年7月18日']
        },
        grid:{
            left: '16%',
            right: '8%',
            bottom: '16%',
            containLabel: true
        },
        xAxis:{
            type: 'value',
            boundaryGap:[0,0.01]
        },
        yAxis: {
            type:'category',
            data : []
        },
        series: [
            {
                name: '2023年7月18日',
                type: 'bar',
                data : []
            }
        ]
    });
    myChart.hideLoading();
    var websocket = null;
    // 判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window){
        websocket = new WebSocket("ws://localhost:8080/uiwebSocket");
    }else {
        alert('当前浏览器不支持WebSocket')
    }

    // 连接发生错误的回调方法
    websocket.onerror = function (){
        setMessageInnerHTML("WebSocket连接发生错误");
    };

    // 连接成功建立的回调方法
    websocket.onopen = function (){
        setMessageInnerHTML("WebSocket连接成功")
    };

    // 接受到消息的回调方法
    websocket.onmessage = function (event){
        jsonbean = JSON.parse(event.data);
        // alert(jsonbean);
        // 填充数据
        myChart.setOption({
            yAxis: {
                data : jsonbean.productId
            },
            series : [{
                // 根据名字对应到相应的系列
                data : jsonbean.produceSumPrice
            }]
        })
        setMessageInnerHTML(event.data);
    }

    // 连接关闭的回调方法
    websocket.onclose= function (){
        setMessageInnerHTML("WebSocket连接关闭");
    };

    // 监听窗口关闭时间，当窗口关闭时，出动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会爆出异常
    window.onbeforeunload = function (){
        closeWebSocket();
    };

    // 将消息显示在网页上
    function setMessageInnerHTML(innerHTML){
        // document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //关闭WebSocket连接
    function closeWebSocket(){
        websocket.close();
    }
</script>
</body>
</html>