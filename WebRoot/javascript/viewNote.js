Ext.onReady(function(){
	Ext.Ajax.timeout = 180000;
	Ext.lib.Ajax.defaultPostHeader = Ext.lib.Ajax.defaultPostHeader + '; charset=UTF-8'; 
	getAll();
});
function getAll(){
	
	Ext.Ajax.request({
		url : 'myNote?action=getAll',
		success : function(response,option){
			var json = Ext.util.JSON.decode(response.responseText);
			var box = document.getElementById('div_noteList');
			box.innerHTML = '';
			//console.log(json.length);
			if(json== undefined || json == null) return ;
			Ext.each(json,function(item,index,list){
				var htmlItem = document.createElement('div');
				htmlItem.innerHTML = '<a href="javascript:void(0);" onclick="getOneNote(\'' + list[index].noteId + '\');">' + list[index].title + '</a>';
				box.appendChild(htmlItem);
			});
		},
		failure : function(options){
			alert('失败！');
		}
	});
}
function getOneNote(noteId){
	Ext.Ajax.request({
		url : 'myNote?action=getOne&noteId=' + noteId,
		success : function(response,option){
			var json = Ext.util.JSON.decode(response.responseText);
			var box = document.getElementById('div_noteContent');
			box.innerHTML = '';
			var head = document.createElement('div');
			head.setAttribute('style','text-align:center');
			head.innerHTML =  json.title ;
			box.appendChild(head);
			var action = document.createElement('div');
			action.innerHTML = '<a href="javascript:void(0);" onclick="deleteNote(\'' + noteId + '\');">删除</a>' + 
			'<a href="javascript:void(0);" onclick="editNote(\'' + noteId + '\');">编辑</a>';
			box.appendChild(action);
			
			var viewCount = document.createElement('div');
			viewCount.innerHTML = '<p>浏览次数：' + json.viewCount + '</p>';
			box.appendChild(viewCount);
			
			var body = document.createElement('div');
			body.innerHTML = json.content;
			box.appendChild(body);
			increaseViewCount(json.noteId);
		},
		failure : function(options){
			alert('失败！');
		}
	});
}
function editNote(noteId){
	window.location.href = 'note.html?action=edit&noteId=' + noteId;
}
function increaseViewCount(noteId){
	Ext.Ajax.request({
		url : 'myNote?action=increaseViewCount&noteId=' + noteId,
		success : function(){
		},
		failure : function(options){
			alert('失败！');
		}
	});
}
function deleteNote(noteId){
	Ext.Msg.confirm('删除确认','确定要删除此记录吗？',function(btn){
		if(btn=='yes'){
				Ext.getBody().mask('loading...');
			Ext.Ajax.request({
				url : 'myNote?action=deleteOne&noteId=' + noteId,
				success : function(response,option){
					getAll();
					Ext.getBody().unmask();
				},
				failure : function(options){
					alert('失败！');
					Ext.getBody().unmask();
				}
			});
		}
	})
	
}
