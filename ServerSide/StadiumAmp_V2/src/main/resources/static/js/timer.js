let min, sec;
let timer;
let serverId;
function startTimer() {
    min = document.querySelector("#startMin").value;
    if (min === "") min = 0;
    sec = document.querySelector("#startSec").value;
    if (sec === "") sec = 0;
    // init time text
    if (sec != 0) {
        sec--;
        document.querySelector("#display").innerText =
            "현재 응원 이벤트 남은 시간 " + min + "분" + sec + "초";
    } else {
        if (min != 0) {
            min--;
            sec = 59;
        } else {
            clearTimer(timer, "이벤트 종료");
        }
    }

    // update timer
    timer = setInterval(countTimer, 1000);

    //serverId = document.querySelector("#startIdVal").value;
    serverId = document.getElementById("startIdVal").value;
    initGraph();
}

function startTimer2() {
    min = document.querySelector("#startMin").value;
    if (min === "") min = 0;
    sec = document.querySelector("#startSec").value;
    if (sec === "") sec = 0;
    // init time text
    if (sec != 0) {
        sec--;
        document.querySelector("#display").innerText =
            "현재 응원 이벤트 남은 시간 " + min + "분" + sec + "초";
    } else {
        if (min != 0) {
            min--;
            sec = 59;
        } else {
            clearTimer(timer, "이벤트 종료");
        }
    }

    // update timer
    timer = setInterval(countTimer2, 1000);

    //serverId = document.querySelector("#startIdVal").value;
    serverId = document.getElementById("startIdVal").value;
//    initGraph();
    updateGraph();
}

function countTimer() {
    //console.log("countTimer : ")
    updateGraph();
    $.ajax({
        url:  '/v1/event/eventdto',
        method: 'get',
        data: {
            serverId: serverId
        }
    }).done(function(res) {
        if (res)
        {
            console.log("event-state ", res);
            document.querySelector("#display").innerText = res.data.eventState;

            const graph_stop =document.getElementById("event_stop_graph");
            const graph_start =document.getElementById("event_start_graph");

            if(res.data.eventState == "이벤트 종료")
            {
                graph_stop.style.display = 'flex';
                graph_start.style.display = 'none';

                // document.getElementById("home_text").innerText = "Home";
                // document.getElementById("away_text").innerText = "Away";

                document.getElementById("home_text").innerText = res.data.homeName;
                document.getElementById("away_text").innerText = res.data.awayName;

                // document.getElementById("event_stop_graph").style.display ='flex';
                // document.getElementById("event_start_graph").style.display ='none';
            }
            else
            {
                graph_stop.style.display = 'none';
                graph_start.style.display = 'flex';
                // document.getElementById("event_stop_graph").style.display ='none';
                // document.getElementById("event_start_graph").style.display ='flex';

                const homeTextElement = document.getElementById("home_text");
                if (res.data.homeCount > 0)
                {
                    const homeText = homeTextElement.innerText;
                    const match = homeText.match(/\d+$/);
                    if(match)
                    {
                        prevCount = parseInt(match[0], 10);
                        if(prevCount != res.data.homeCount){
                            homeTextElement.classList.add('grow');
                            setTimeout(() => {
                                homeTextElement.classList.remove('grow');
                            }, 200);
                        }
                    }
                    else {
                        homeTextElement.classList.add('grow');
                        setTimeout(() => {
                            homeTextElement.classList.remove('grow');
                        }, 200);
                    }

                    homeTextElement.innerText = res.data.homeName + " " + res.data.homeCount;
                }
                else {
                    homeTextElement.innerText = res.data.homeName;
                }

                const awayTextElement = document.getElementById("away_text");
                if (res.data.awayCount > 0){
                    const awayText = awayTextElement.innerText;
                    const match = awayText.match(/\d+$/);
                    if(match)
                    {
                        prevCount = parseInt(match[0], 10);
                        if(prevCount != res.data.awayCount){
                            awayTextElement.classList.add('grow');
                            setTimeout(() => {
                                awayTextElement.classList.remove('grow');
                            }, 200);
                        }
                    }
                    else {
                        awayTextElement.classList.add('grow');
                        setTimeout(() => {
                            awayTextElement.classList.remove('grow');
                        }, 200);
                    }

                    awayTextElement.innerText = res.data.awayCount + " " +res.data.awayName;
                }
                else{
                    awayTextElement.innerText = res.data.awayName;
                }
                //window.navigator.vibrate(200);
                console.log("res.data.isStart ", res.data.isStart);
                if(res.data.isStart)
                    window.navigator.vibrate(500);


            }
        }
        else
        {

        }
    });

    // if (sec != 0) {
    //     sec--;
    //     document.querySelector("#display").innerText =
    //         "현재 응원 이벤트 남은 시간 " + min + "분" + sec + "초";
    // } else {
    //     if (min != 0) {
    //         min--;
    //         sec = 59;
    //     } else {
    //         clearTimer(timer, "이벤트 종료");
    //     }
    // }
}

function createConfetti() {
    for (let i = 0; i < 100; i++) {
        let confetti = document.createElement('div');
        confetti.classList.add("confetti");
        confetti.style.backgroundColor = getRandomColor();
        confetti.style.left = "50vw";
        confetti.style.top = "50vh";
        confetti.style.setProperty('--x', (Math.random() - 0.5) * 200 + "vw");
        confetti.style.setProperty('--y', (Math.random() - 0.5) * 200 + "vh");
        document.body.appendChild(confetti);

        setTimeout(() => {
            confetti.remove();
        }, 2000);
    }
}


function countTimer2() {
    console.log("countTimer : ")
    $.ajax({
        url:  '/v1/event/eventdto',
        method: 'get',
        data: {
            serverId: serverId
        }
    }).done(function(res) {
        if (res)
        {
            console.log("res", res);
            document.querySelector("#display").innerText = res.data.eventState;

            // const graph_stop =document.getElementById("event_stop_graph");
            // const graph_start =document.getElementById("event_start_graph");
            // if(res.data.eventState == "이벤트 종료")
            // {
            //     graph_stop.style.display = 'flex';
            //     graph_start.style.display = 'none';
            //     // document.getElementById("event_stop_graph").style.display ='flex';
            //     // document.getElementById("event_start_graph").style.display ='none';
            // }
            // else
            // {
            //     graph_stop.style.display = 'none';
            //     graph_start.style.display = 'flex';
            //     // document.getElementById("event_stop_graph").style.display ='none';
            //     // document.getElementById("event_start_graph").style.display ='flex';
            // }
        }
        else
        {

        }
    });
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

            if(res.data.eventState == "이벤트 종료")
            {
                document.getElementById("event_stop_graph").style.display ='flex';
                document.getElementById("event_start_graph").style.display ='none';
            }
            else
            {
                document.getElementById("event_stop_graph").style.display ='none';
                document.getElementById("event_start_graph").style.display ='flex';
            }

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


function updateGraph()
{
    console.log("updateGraph : ")
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