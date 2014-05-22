var cp;

Ext.onReady(function(){
	cp = new Ext.state.CookieProvider({
		expires: new Date(new Date().getTime()+(1000*60*60*24*30)) //30 days
	});
	Ext.state.Manager.setProvider(cp);//set default cp
	var dailyPanel = new DailyPanel({id: 'dailyPanel'});
	var hisPanel = new HisPanel({});
	var hisForm = new Ext.form.FormPanel({
		id : 'hisForm',
		items : hisPanel,
		renderTo : 'hisPanel'
	});
	Ext.lib.Ajax.defaultPostHeader = Ext.lib.Ajax.defaultPostHeader + '; charset=UTF-8'; 
	//console.log(hisPanel.tbar11);
	//hisPanel.tbar11.add({text:'add1'});
	
});

DailyPanel = function(config){
	var familyItemStoreData = [
		{id : '买菜',name : '买菜'},
		{id : '超市',name : '超市'},
		{id : '早饭',name : '早饭'},
		{id : '中饭',name : '中饭'},
		{id : '饮用水',name : '饮用水'},
		{id : '水费',name : '水费'},
		{id : '电费',name : '电费'},
		{id : '宽带',name : '宽带'},
		{id : '其他',name : '其他'}
	];
	var personStoreData = [
		{id : 'CCH',name : 'CCH'}
	];
	var itemTypeStoreData = [
		{id:'normal',name:'normal'},
		{id:'investment',name:'investment'}
	];
	//familyItemStore.loadData(familyItemStoreData);
	DailyPanel.superclass.constructor.call(this,{
		id : 'form_newDaily',
		title : '费用',
		width : 500,
		height : 200,
		defaultType: 'textfield',
		items:[
			new Ext.form.DateField({
				fieldLabel: '日期',
				name: 'daily_date',
				width : 130,
				allowBlank:false,
				altFormats: 'Y-m-d',
				format:'Y-m-d',
				emptyText:'Select a date...',
				value : new Date()
			}),{
				fieldLabel: '事项',
				xtype : 'combo',
				name: 'daily_item',
				valueField : 'name',
				displayField : 'name',
				store : new Ext.data.JsonStore({
					data : familyItemStoreData,
					fields : ['id','name']
				}),
				width : 130,
				mode : 'local', //set to load data in local
				triggerAction : 'all', //if not set to all, it will filter the data
				emptyText : '水费'
			},{
				fieldLabel: '姓名',
				id : 'daily_name',
				name: 'daily_name',
				//value : cp.get('personName',''), //default value in cookie
				allowBlank:false,
				xtype : 'combo',
				store : new Ext.data.JsonStore({
					data : personStoreData,
					fields : ['id','name']
				}),
				//store : personItemStore,
				valueField : 'name',
				displayField : 'name',
				width : 130,
				mode : 'local', //set to load data in local
				triggerAction : 'all', //if not set to all, it will filter the 
				emptyText : 'CCH'
			},new Ext.form.NumberField({
				fieldLabel: '金额',
				name: 'daily_money',
				allowBlank:false,
				allowDecimals :true
			}),
			{
				fieldLabel: '类型',
				xtype : 'combo',
				name: 'daily_type',
				valueField : 'name',
				displayField : 'name',
				store : new Ext.data.JsonStore({
					data : itemTypeStoreData,
					fields : ['id','name']
				}),
				width : 130,
				mode : 'local', //set to load data in local
				triggerAction : 'all', //if not set to all, it will filter the data
				emptyText : 'normal'
			}
		],
		renderTo : config.id,
		buttons : [
			new Ext.Button({text : '确定',
				handler : function(){
					
					cp.set('personName',Ext.getCmp('daily_name').getValue()); //set userName
					Ext.Ajax.request({
						url : 'Actions.aspx?action=add',
						form : Ext.getCmp('form_newDaily').getForm().el.dom,
						success : function(response,option){
							Ext.example.msg('Done', '成功!');
							Ext.StoreMgr.get('hisStore').reload();
						},
						failure : function(options){
							alert('失败');
						}
					});
				}
			})
		]
	});
};
HisPanel = function(config){
	//Added by Chunhui Chen 2014-04-27 used for 'normal', 'investment'
	var itemType = 'normal';
	var myStore = new Ext.data.JsonStore({
		id : 'hisStore',
		autoLoad : true,
		url : 'Actions.aspx?action=getData',
		fields : ['feeDate','itemName','fee','personName','id','IsPaid']
	});
	personStoreData = [
		{id : 'CCH',name : 'CCH'},
		{id : 'CJX',name : 'CJX'},
		{id : 'LJW',name : 'LJW'},
		{id : 'ZZQ',name : 'ZZQ'}
	];
	personItemStore = new Ext.data.JsonStore({
		data : personStoreData,
		fields : ['id','name']
	});
	var edit1 = new Ext.form.TextField({
					enableKeyEvents:true,
					listeners : {
						keyup : function(){alert('x');},
						change : function(){alert('y');}
					}
	});
	var toolbar1 = new Ext.Toolbar([
		{text:'add'}
	]);
	//console.log(toolbar1);
	//toolbar1.items.add(new Ext.Toolbar.Item({text:'22222',iconCls:'ddd'}))//addSeparator(); 
	//console.log(toolbar1);
	//toolbar1.add(new Ext.Toolbar.Item({text:'add'}));
	//edit1.on('keyup',function(){alert('x');},this,{buffer:200});
	var sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	HisPanel.superclass.constructor.call(this,{
		title : '历史数据',
		loadMask : true,
		height : 600,
		width : 500,
		sm : sm,
		cm :  new Ext.grid.ColumnModel([
			sm,
			// {header : '', dataIndex : 'ID', width : 20, hidden:true,
				// renderer : function(value,p,record){
					// return '<input type="checkbox" name="IDs" value=" '+ record.data.ID +'" />';
				// }
			// },
			{header : '日期', dataIndex : 'feeDate', sortable: true,
				renderer :  Ext.util.Format.dateRenderer('Y-m-d'),
					editor : new Ext.form.DateField({
						fieldLabel: '日期',
						name: 'daily_date',
						width : 130,
						allowBlank:false,
						altFormats: 'Y-m-d',
						format:'Y-m-d',
						emptyText:'Select a date...',
						value : new Date()
					})
			},
			{header : '事项', dataIndex : 'itemName', sortable: true,
				editor : edit1
			},
			{header : '金额', dataIndex : 'fee', sortable: true,
				renderer :  Ext.util.Format.usMoney
			},
			{header : '经手人', dataIndex : 'personName', sortable: true,
				editor : new Ext.form.ComboBox({
					valueField : 'name',
					displayField : 'name',
					// store : new Ext.data.JsonStore({
						// data : personStoreData,
						// fields : ['id','name']
					// }),
					store : personItemStore,
					width : 130,
					mode : 'local', //set to load data in local
					triggerAction : 'all' //if not set to all, it will filter the data
				})
			}
		]),
		store : myStore,
		tbar : [
			{text:'刷新',
				handler : function(){
					myStore.load();
				}
			},
			{
				text : '删除',
				handler : function(){
					if(!sm.hasSelection()) return ;
					var Ids = "";
					//console.log(sm.getSelections());
					Ext.each(sm.getSelections(),function(item,index,items){
						Ids=Ids+item.data.id+',';
						//console.log(item.data);
					});
					
					if(Ids.length>0) Ids=Ids.substring(0,Ids.length-1);
					Ext.Ajax.request({
						url : 'Actions.aspx?action=delete',
						//form : Ext.getCmp('hisForm').getForm().el.dom,
						params : {
							IDs: Ids
						},
						success : function(response,option){
							Ext.example.msg('Done', 'Delete success!');
							myStore.reload();
						},
						failure : function(option){
							Ext.example.msg('ERROR', 'Delete failed!');
						}
					});
				}
			},
			{text:'统计',
				handler : function(){
					Calculator(sm.getSelections());
				}
			},
			new Ext.Toolbar.MenuButton({
				text : 'normal',
				iconCls: 'blist',
				handler : function(btn){
					itemType = 'normal';
					myStore.load({
						params : {
							itemType : itemType,
							random : new Date().format('U')
						}
					});
				},
				menu : {
					items : [
						{text : 'normal',handler : function(item){
							itemType = 'normal';
							myStore.load({
								params : {
									itemType : itemType,
									random : new Date().format('U')
								}
							});
						}},
						{
							text : 'investment',
							handler	: function(item){
								itemType = 'investment';
								myStore.load({
								params : {
									itemType : itemType,
									random : new Date().format('U')
								}
							});
							}
						}
					]
				}
			})
			// {text:'确认已结算',
				// handler : function(){
					// if(!sm.hasSelection()) return ;
					// if(Ext.Msg.confirm('确认','确定已结算吗？',function(btn){
						// if(btn=='yes'){
							// var Ids = "";
							// Ext.each(sm.getSelections(),function(item,index,items){
								// Ids=Ids+item.data.ID+',';
							// });
							// if(Ids.length>0) Ids=Ids.substring(0,Ids.length-1);
							// Ext.Ajax.request({
								// url : 'Actions.aspx?action=confirmPaid',
								// //form : Ext.getCmp('hisForm').getForm().el.dom,
								// params : {
									// IDs: Ids
								// },
								// success : function(response,option){
									// Ext.example.msg('Done', 'Confirm success!');
									// myStore.reload();
								// },
								// failure : function(option){
									// Ext.example.msg('ERROR', 'Confirm failed!');
								// }
							// });
						// }
					// }));
					
				// }
			// }
		]
		//tbar :  toolbar1
	});
	this.tbar11 = toolbar1;
	this.on({
		'afteredit' : function(){
			var r = myStore.getModifiedRecords()[0];
			Ext.Ajax.request({
				url : 'Actions.aspx?action=update',
				params : {
					'ID' : r.data.ID,
					'itemName' : r.data.itemName,
					'fee' : r.data.fee,
					'feeDate' : r.data.feeDate,
					'personName' : r.data.personName
				},
				success : function(response,option){
							Ext.example.msg('Done', 'Update success!');
							myStore.reload();
						},
				failure : function(option){
				            Ext.example.msg('ERROR', 'Update failed!');
						}
			});
			myStore.modified=[];
			//myStore.reload();
		}
	});
	var Calculator = function(items){
		var result={};
		var itemIndex=[];
		//result.totalFee=0;
		result.length=0;
		Ext.each(items,function(item,index,items){
			if(!result[item.data.itemName]){
				result[item.data.itemName] = {itemName:item.data.itemName};
				result[item.data.itemName].count = 0;
				result[item.data.itemName].totalFee = 0;
				result.length=result.length+1;
				itemIndex.push(item.data.itemName);
			}
			result[item.data.itemName].totalFee += item.data.fee;
			result[item.data.itemName].count += 1;
		});
		var calculateStore={};
		calculateStore.data=[];
		var sumupItem = {itemName:'小计',count:1,totalFee:0,avgFee:0};
		Ext.each(itemIndex,function(item,index,items){
			result[item].avgFee = result[item].totalFee / result[item].count;
			calculateStore.data.push(result[item]);
			sumupItem.totalFee = sumupItem.totalFee + result[item].totalFee;
		});
		sumupItem.avgFee = sumupItem.totalFee;
		calculateStore.data.push(sumupItem);
		
		var calcPanel = new CalcPanel({data:calculateStore});
		var winCalc = new Ext.Window({
			title : '结果',
			items : [
				new Ext.Button({
					text : '导出',
					handler : function(){
						alert('导出');
					}
				}),
				calcPanel
			],
			autoHeight  : true
			//autoWidth : true
		});
		winCalc.show();
	};
	
}
CalcPanel = function(config){
	var calcStore = new Ext.data.JsonStore({
		fields : ['itemName','count','avgFee','totalFee'],
		data : config.data,
		root :　'data'
	});
	CalcPanel.superclass.constructor.call(this,{
		// title : '计算结果',
		cm : new Ext.grid.ColumnModel([
			//{header : '姓名', dataIndex:'name',width:100},
			{header : '事项', dataIndex:'itemName',width:100
				//renderer : Ext.util.Format.usMoney
			},
			{header : '次数', dataIndex:'count',width:100
				// renderer : Ext.util.Format.usMoney
			},
			{header : '平均', dataIndex:'avgFee',width:100,
				renderer : Ext.util.Format.usMoney
			},
			{header : '总计', dataIndex:'totalFee',width:100,
				renderer : Ext.util.Format.usMoney
			}
		]),
		store : calcStore,
		width : 420
	})
	return this;
}

Ext.extend(DailyPanel,Ext.form.FormPanel,{
});
Ext.extend(HisPanel,Ext.grid.EditorGridPanel,{
});
Ext.extend(CalcPanel,Ext.grid.GridPanel,{
});

Ext.override(Ext.menu.DateMenu,{     
  render : function (){
    Ext.menu.DateMenu.superclass.render.call(this);
        if (Ext.isGecko){
			//firefox
            this.picker.el.dom.childNodes[0].style.width =  '178px' ;
            this.picker.el.dom.style.width =  '178px' ;
        }else if(Ext.isIE){
			//IE please use in compact view
			//this.picker.el.dom.childNodes[0].style.width =  '138px' ;
			//alert(this.picker.el.dom.id);
            this.picker.el.dom.style.width =  '178px' ;
		}else{
			//chrome
			this.picker.el.dom.childNodes[0].style.width =  '178px' ;
            this.picker.el.dom.style.width =  '178px' ;
		}
    }
});
