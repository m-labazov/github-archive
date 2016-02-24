var restClient = {}

function RestClient() {
    this.postCollect = function() {
        var xhttp = new XMLHttpRequest();
        var from = document.getElementById("fromDate").value;
        var to = document.getElementById("toDate").value;
        xhttp.open("POST", "collect/" + from + "/" + to, true);
        xhttp.onreadystatechange = function() {
            if (xhttp.status == 200 && this.readyState === this.DONE) {
              var id = xhttp.responseText;
              document.getElementById("tasks").innerHTML = getTaskDiv(id, from, to, "IN_PROGRESS") + document.getElementById("tasks").innerHTML;
              document.getElementById("task" + id).addEventListener("click", function() {
                restClient.displayResult(id);
              });
              restClient.getStatus(id);
            }
        };
        xhttp.send();
    }
    this.getStatus = function(id) {
        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", "check/" + id, false);
        xhttp.send();
        if (xhttp.status == 200 && xhttp.responseText == "COMPLETE") {
            document.getElementById("taskstatus" + id).innerHTML = xhttp.responseText;
            return;
        } else {
            window.setTimeout(function() {restClient.getStatus(id);}, 2000);
        }
    }
    this.displayResult = function(id) {
        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", "metrics/" + id, false);
        xhttp.send();
        if (xhttp.status == 200) {
            document.getElementById("results").innerHTML = getMetricsDiv(JSON.parse(xhttp.responseText));
            return;
        }
    }
    this.loadTasks = function() {
        var xhttp = new XMLHttpRequest();
        xhttp.open("GET", "tasks", true);
        xhttp.send();
        xhttp.onreadystatechange = function() {
            if (xhttp.status == 200 && this.readyState === this.DONE) {
                var jsonData = JSON.parse(xhttp.responseText);
                document.getElementById("tasks").innerHTML = "";
                for (var i = 0; i < jsonData.length; i++) {
                    var obj = jsonData[i];
                    document.getElementById("tasks").insertBefore(getTaskDiv(obj.id, obj.from, obj.to, obj.status), document.getElementById("tasks").firstChild);
                    document.getElementById(obj.id).addEventListener("click", function() {
                        restClient.displayResult(this.id);
                    });
                }
            }
        }
    }
    this.login = function() {
        var xhttp = new XMLHttpRequest();
        var userId = document.getElementById("userId").value;
        var to = document.getElementById("toDate").value;
        xhttp.open("POST", "login?userId=" + userId, false);
        xhttp.send();
        restClient.loadTasks();
    }
}

function getMetricsDiv(metrics) {
    var result = "<div>Top 5 events: ";
    for (var i = 0; i < metrics.top5Events.length; i++) {
        result += "<div>" + (i + 1) +". " + metrics.top5Events[i] + "</div>";
    }
    result += "</div><div>Top 5 Users: ";
    for (var i = 0; i < metrics.top5Users.length; i++) {
        result += "<div/><div>" + (i + 1) +". " + metrics.top5Users[i] + "</div>";
    }
    result += "</div><div>Top 5 Repos: "
    for (var i = 0; i < metrics.top5Repos.length; i++) {
        result += "<div/><div>" + (i + 1) +". " + metrics.top5Repos[i] + "</div>";
    }
    result += "</div><div/><div>Total number of events: " + metrics.totalEventsNumber;
    result +=    "</div>";
    return result;
}

function getTaskDiv(id, from, to, status) {
    var str = "<div id='" + id + "' class='clickable'>From " + from + " to " + to +
                "<div id='taskstatus" + id + "'>" +
                    status +
                "</div>" +
           "</div>";
    var child = document.createElement('div');
    child.innerHTML = str;
    return child.firstChild
}

function initialize() {
    restClient = new RestClient();
    document.getElementById("searchButton").addEventListener("click", function() {
        restClient.postCollect();
    });
    document.getElementById("loginButton").addEventListener("click", function() {
        restClient.login();
    });
    restClient.loadTasks();
}

