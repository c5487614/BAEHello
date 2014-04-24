Ext.onReady(function(){
	Ext.lib.Ajax.defaultPostHeader = Ext.lib.Ajax.defaultPostHeader + '; charset=UTF-8';
	Ext.Ajax.request({
		url : 'sp/lotteryAnalysize.do',
		success : function(response,option){
			var json = Ext.util.JSON.decode(response.responseText);
			var divMain = document.getElementById('div_main');
			var divRed = document.getElementById('div_red');
			var table = document.createElement('table');
			var i = 1;
			for(;i<33;i++){
				var tr = document.createElement('tr');
				var td = createTD();
				td.innerHTML = '' + i;
				tr.appendChild(td);
				td = createTD();
				td.innerHTML = '' + json.red[''+i];
				tr.appendChild(td);
				table.appendChild(tr);
			}
			divRed.appendChild(table);
			table = document.createElement('table');
			for(i=1;i<17;i++){
				var tr = document.createElement('tr');
				var td = createTD();
				td.innerHTML = '' + i;
				tr.appendChild(td);
				td = createTD();
				td.innerHTML = '' + json.blue[''+i];
				tr.appendChild(td);
				table.appendChild(tr);
			}
			var divBlue = document.getElementById('div_blue');
			divBlue.appendChild(table);
			
			
			table = document.createElement('table');
			var tr = document.createElement('tr');
			for(i=1;i<8;i++){
				var td = createTD();
				td.innerHTML = '' + json.redPredict[''+i];
				tr.appendChild(td);
			}
			table.appendChild(tr);
			var divRedPredict = document.getElementById('div_redPredict');
			divRedPredict.appendChild(table);
			
			table = document.createElement('table');
			var tr = document.createElement('tr');
			for(i=1;i<3;i++){
				var td = createTD();
				td.innerHTML = '' + json.bluePredict[''+i];
				tr.appendChild(td);
			}
			table.appendChild(tr);
			var divBluePredict = document.getElementById('div_bluePredict');
			divBluePredict.appendChild(table);
			
			var history = json.match;
			var tablePredict = document.createElement('table');
			var tableOpen = document.createElement('table');
			var j = 0;
			if(history.predictHistory.length==0) return ;
			for(i=0;i<history.predictHistory.length;i++){
				var predict = history.predictHistory[i];
				var opened = history.openHistory[j];
				var trPredict = document.createElement('tr');
				var trOpen = document.createElement('tr');
				var tdOpen = createTD();
				tdOpen.innerHTML = predict.numIndex;
				trOpen.appendChild(tdOpen);
				if(opened && predict.numIndex==opened.numIndex){
					//if predict and opened both have
					for(var k = 1;k<8;k++){
						tdOpen = createTD();
						tdOpen.innerHTML = opened['ball'+k];
						trOpen.appendChild(tdOpen);
						var tdPredict = createTD();
						tdPredict.innerHTML = predict['ball'+k];
						trPredict.appendChild(tdPredict);
						if(k==7){
							tdOpen.style.backgroundColor = '#eee';
							tdPredict.style.backgroundColor = '#eee';
						}
						for(var m = 1;m < 8;m++){
							if(predict['ball'+k]==opened['ball'+m] && m!=7 && k!=7){
								tdPredict.style.color = '#00FF00';
							}
							if(k==7 && m==7 && predict['ball7'] == opened['ball7']){
								tdPredict.style.color = '#00FF00';
							}
						}
					}
					tableOpen.appendChild(trOpen);
					tablePredict.appendChild(trPredict);
					j++;
				}else{
					for(var k = 1;k<8;k++){
						var tdOpen = createTD();
						tdOpen.innerHTML = '';
						trOpen.appendChild(tdOpen);
						var tdPredict = createTD();
						tdPredict.innerHTML = predict['ball'+k];
						if(k==7){
							tdPredict.style.backgroundColor = '#eee';
						}
						trPredict.appendChild(tdPredict);
					}
					tableOpen.appendChild(trOpen);
					tablePredict.appendChild(trPredict);
				}
			}
			document.getElementById('div_opened').appendChild(tableOpen);
			document.getElementById('div_predict').appendChild(tablePredict);
		},
		failure : function(options){
			alert('Ê§°Ü£¡');
		}
	});
});
function createTD(){
	var td = document.createElement('td');
	td.style.border = "solid #eee 1px";
	td.style.width = "30px";
	td.style.height = "30px";
	td.style.textAlign = "center";
	td.style.verticalAlign="middle";
	return td;
}