Ext.QuickTips.init();
Ext.onReady(function(){
	Ext.lib.Ajax.defaultPostHeader = Ext.lib.Ajax.defaultPostHeader + '; charset=UTF-8'; 
	Ext.Ajax.timeout = 180000;
	var note = new MyNote({renderTo : 'div_main'});
});
var regionList = [
	{text : 'C#/.Net',value : 'C#/.Net'},
	{text : 'asp.net',value : 'asp.net'},
	{text : 'java',value : 'java'},
	{text : 'IIS',value : 'IIS'},
	{text : 'android',value : 'android'},
	{text : 'C/C++',value : 'C/C++'},
	{text : 'javascript',value : 'javascript'},
	{text : 'html',value : 'html'},
	{text : 'html5',value : 'html5'},
	{text : 'css',value : 'css'},
	{text : 'windows',value : 'windows'},
	{text : 'work',value : 'work'},
	{text : 'server',value : 'server'},
	{text : 'other',value : 'other'}
];
function regionInit(arrayConfig,containerId){
	var arrRegion = [];
	for(var item in arrayConfig){
		if(arrayConfig[item].text){
			arrRegion.push(new Ext.form.Checkbox({
				columnWidth : .1,
				boxLabel : arrayConfig[item].text,
				name : 'cbox_'+arrayConfig[item].value,
				id : 'cbox_'+arrayConfig[item].value,
				listeners : {
					check : function(){
						var region = '';
						Ext.each(Ext.get(containerId).query('input'),function(item,index,array){
							if(item.checked){
								region = region + arrayConfig[index].value + ',';
							}
						});
						if(region[region.length-1]==','){
							region = region.substring(0,region.length-1);
						}
						Ext.getCmp('tbox_region').setValue(region);
					}
				}
			}));
		}
	}
	return arrRegion;
}
function getNoteEdit(noteId){
	Ext.Ajax.request({
		url : 'myNote?action=getOne&noteId=' + noteId,
		success : function(response,option){
			var json = Ext.util.JSON.decode(response.responseText);
			Ext.getCmp('tbox_title').setValue(json.title);
			Ext.getCmp('tbox_content').setValue(json.content);
			Ext.getCmp('tbox_region').setValue(json.region);
			var regions = json.region;
			var regs = regions.split(',');
			var i = 0;
			var cboxs = Ext.get('panel_region').query('input');
			var cboxCmp;
			for(var cbox in cboxs){
				cboxCmp = Ext.getCmp(cboxs[cbox].id);
				if(cboxCmp){
					if(regs[i]==cboxCmp.boxLabel){
						cboxCmp.setValue(true);
						i++;
					}
				}
			}
		},
		failure : function(options){
			alert('失败！');
		}
	});
}
var MyNote = function(config){
	MyNote.mode = '';
	MyNote.superclass.constructor.call(this,{
		id : 'frmNote',
		height : 'auto',
		width : 'auto',
		title : 'Write a note',
		style : 'margin-top:10px;margin-left:10px;margin-right:10px',
		bodyStyle : '',
		listeners : {
			afterlayout : function(){
				//http://localhost:8080/cchapps/note.html?action=endit&noteId=dadsa
				var url = window.location.href;
				var params = url.substring(url.indexOf('?')+1).split('&');
				var param = {};
				for(var p in params){
					if( typeof params[p] == 'string'){
						var key = params[p].split('=')[0];
						var value = params[p].split('=')[1];
						param[key] = value; 
					}
				}
				if(params.length>1){
					var mode = param['action'];
					if(mode == 'edit'){
						MyNote.mode = 'edit';
						//console.log(Ext.getCmp('tbox_noteId'))
						Ext.getCmp('tbox_noteId').setValue(param['noteId']);
						Ext.getCmp('btn_add').hide();
						Ext.getCmp('btn_edit').show();
						getNoteEdit(param['noteId']);
						
					}
				}
				
			}
		},
		items : [
			new Ext.form.Hidden({
				name : 'tbox_user',
				value : 'CCH'
			}),
			new Ext.form.Hidden({
				name : 'tbox_noteId',
				id : 'tbox_noteId',
				value : ''
			}),
			new Ext.form.TextField({
				fieldLabel : '标题',
				name : 'tbox_title',
				id : 'tbox_title',
				labelStyle : 'margin-top:10px;',
				style : 'margin-top:10px;',
				width : 700
			}),
			new Ext.form.TextField({
				fieldLabel : '领域',
				id : 'tbox_region',
				name : 'tbox_region',
				labelStyle : 'margin-top:10px;',
				style : 'margin-top:10px;',
				width : 700
			}),
			new Ext.Panel({
				border : false,
				style : 'margin-left:105px;font:12px tahoma,arial,helvetica,sans-serif;',
				layout:'column',
				cls : '.x-form-item',
				id : 'panel_region',
				width : 700,
				items : regionInit(regionList,'panel_region')
			}),
			// new Ext.Panel({
				// border : false,
				// style : 'margin-left:105px;font:12px tahoma,arial,helvetica,sans-serif;',
				// layout:'column',
				// cls : '.x-form-item',
				// id : 'panel_region1',
				// items : regionInit(regionList1,'panel_region1')
			// }),
			new Ext.form.HtmlEditor({
				name : 'tbox_content',
				id : 'tbox_content',
				fieldLabel : '内容',
				width : 700,
				height : 200
			})
		],
		buttonAlign : 'left',
		buttons : [
			new Ext.Button({
				text : '提交',
				id : 'btn_add',
				handler : function(){
					Ext.Ajax.request({
						url : 'myNote?action=add',
						form : Ext.getCmp('frmNote').getForm().el.dom,
						success : function(response,option){
							Ext.example.msg('Done', '成功！');
						},
						failure : function(options){
							alert('失败！');
						}
					});
				}
			}),
			new Ext.Button({
				text : '编辑',
				id : 'btn_edit',
				hidden : true,
				handler : function(){
					Ext.Ajax.request({
						url : 'myNote?action=edit',
						form : Ext.getCmp('frmNote').getForm().el.dom,
						success : function(response,option){
							Ext.example.msg('Done', '成功！');
						},
						failure : function(options){
							alert('失败！');
						}
					});
				}
			}),
			new Ext.Button({
				text : '取消'
			})
		],
		renderTo : config.renderTo
	});
}
Ext.extend(MyNote,Ext.form.FormPanel,{});
