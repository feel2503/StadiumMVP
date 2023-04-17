let min, sec;
let timer;
let serverId;
function startTimer() {
    min = document.querySelector("#startMin").value;
    if (min === "") min = 0;
    sec = document.querySelector("#startSec").value;
    if (sec === "") sec = 0;
    timer = setInterval(countTimer, 1000);

    //serverId = document.querySelector("#startIdVal").value;
    serverId = document.getElementById("startIdVal").value;
    initGraph();
}
function countTimer() {
    if (sec != 0) {
        sec--;
        document.querySelector("#display").innerText =
            min + "분" + sec + "초 남았습니다.";
    } else {
        if (min != 0) {
            min--;
            sec = 59;
        } else {
            clearTimer(timer, "이벤트 종료");
        }
    }
}
function resetTimer() { // 리셋 버튼 연결
    clearTimer(timer, "리셋 종료");
}
function clearTimer(t, text) {
    clearInterval(t);
    document.getElementById("display").innerText = text;
    document.getElementById("startMin").value = "";
    document.getElementById("startSec").value = "";
}

function initGraph()
{
    console.log("initGraph : ")
    $.ajax({
        url:  '/v1/event/eventdto',
        method: 'get',
        data: {
            serverId: serverId
        }
    }).done(function(res) {
        if (res) {
            console.log("res", res);
            var homeGraph = document.getElementById("home_graph");
            homeGraph.style.flexBasis=res.data.home;
            if(res.data.home === "100%")
                homeGraph.style.borderRadius="10px";
            else{
                homeGraph.style.borderTopRightRadius="0px";
                homeGraph.style.borderBottomRightRadius="0px";
            }
            var awayGraph = document.getElementById("away_graph");
            awayGraph.style.flexBasis=res.data.away;
            if(res.data.away === "100%")
                awayGraph.style.borderRadius="10px";
            else{
                awayGraph.style.borderTopLeftRadius="0px";
                awayGraph.style.borderBottomLeftRadius="0px";
            }
        } else {

        }
    });

}