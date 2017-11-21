String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {  
    if (!RegExp.prototype.isPrototypeOf(reallyDo)) {  
        return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);  
    } else {  
        return this.replace(reallyDo, replaceWith);  
    }  
}
;
String.prototype.endWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substring(this.length-str.length)==str)
	  return true;
	else
	  return false;
	return true;
};
String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substr(0,str.length)==str)
	  return true;
	else
	  return false;
	return true;
};
Date.prototype.format = function(fmt) {
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};
(function($) {
	
	var SUCCESS = 0;
	
	var zTree;
	
	var setting = {
			view: {
				showIcon: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			edit: {
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: false
			},
			callback : {
				onClick : function(event, treeId, treeNode) {
					var path = treeNode.path;
					if (!treeNode.isLoaded)
						loadPath(treeNode, path, true);
					$('#nav').empty();
					
					var view = $('<li><a href="javascript:;"><i class="glyphicon glyphicon-eye-open"></i> View</a></li>');
					view.find('a').click(function() {
						loadPath(treeNode, path, false, function() {
							var body = $('<div class="row">');
							
							var left = $('<div class="col-md-4">');
							body.append(left);
							
							var $stat = $('<ul class="list-group">');
							var stat = treeNode.data.stat;
							for (var key in stat) {
								var value = stat[key];
								if (key == 'ctime' || key == 'mtime') {
									var date = new Date();
									date.setTime(value);
									value = date.format('yyyy-MM-dd hh:mm:ss');
								}
								$stat.append('<li class="list-group-item">' + key + '<span class="badge">' + value + '</span></li>');
							}
							left.append($stat);
							
							var right = $('<div class="col-md-8">');
							body.append(right);
							
							var textarea = $('<textarea rows="26" style="font-family: Courier New;">').addClass('form-control');
							if (treeNode.data) {
								if (treeNode.data.data2String) {
									textarea.val(treeNode.data.data2String);
								}
							}
							
							var _textareaval = textarea.val();
							right.append(textarea);
							body.append(left).append(right);
							
							Modal.open({
								title : path,
								body : body,
								backdrop : 'static',
								size : 'lg',
								shown : function() {
									// textarea.focus();
								},
								btns : {
									ok : {
										text : 'Save',
										onClick : function(e, dialog) {
											if (_textareaval == textarea.val()) {
												Modal.close();
												return;
											}
											savePath(path, textarea.val(), function() {
												Modal.close(function() { alert('Save successful!'); });
											});
										}
									},
									close : { text : 'Close' }
								}
							});
						});
					});
					$('#nav').append(view);
					
					var refresh = $('<li><a href="javascript:;"><i class="glyphicon glyphicon-refresh"></i> Refresh</a></li>');
					refresh.find('a').click(function() {
						treeNode.isLoaded = false;
						$(event.target).click();
					});
					$('#nav').append(refresh);
					
					var add = $('<li><a href="javascript:;"><i class="glyphicon glyphicon-plus"></i> Create Child</a></li>');
					add.find('a').click(function() {
						var body = $('<div>');
						
						var ipt = $('<input>').addClass('form-control').attr('placeholder', 'Child Node Name');
						body.append(ipt);
						
						var fn = function() {
							if (ipt.val() == '' || ipt.val().length == 0) {
								ipt.focus();
								return;
							}
							if (ipt.val().indexOf('/') != -1) {
								alert('Child node name cannot contain character "/"!');
								return;
							}
							var childPath = path + '/' + ipt.val();
							if (path.endWith('/')) {
								childPath = path + ipt.val();
							}
							savePath(childPath, '', function() {
								Modal.close(function() {
									var childTreeNode = convertPath(childPath);
									zTree.addNodes(treeNode, childTreeNode);
								});
							});
						}
						
						Modal.open({
							title : 'Create Child Node',
							body : body,
							backdrop : 'static',
							shown : function() { 
								ipt.focus(); 
								ipt.keyup(function(e) {
									if (e.keyCode == 13) {
										fn();
									}
								});
							},
							btns : {
								ok : {
									text : 'Confirm Create',
									onClick : function(e, dialog) {
										fn();
									}
								},
								close : { text : 'Close' }
							}
						});
					});
					$('#nav').append(add);
					
					var del = $('<li><a href="javascript:;"><i class="glyphicon glyphicon-trash"></i> Delete</a></li>');
					del.find('a').click(function() {
						if (confirm("Are you sure to delete this node '"+treeNode.path+"'? All of its child nodes will also be deleted!")) {
							deletePath(treeNode.path, function() {
								zTree.removeNode(treeNode);
								$('#nav').empty();
							});
						}
					});
					$('#nav').append(del);
				}
			}
	};
	
	var node = {
		
		loadDefault: function() {
			NProgress.start();
			$.getJSON(window.baseUrl+'/path.html?path=/', function(json) {
				NProgress.done();
				if (ok(json)) {
					var data = json.data;
					var childrens = data.childrens;
					
					var zNodes = convertNodes(childrens);
					
					$.fn.zTree.init($("#tree"), setting, zNodes);
					zTree = $.fn.zTree.getZTreeObj("tree");
				}
			});
		}
	};
	
	function loadPath(parentNode, path, appendNodes, callback) {
		NProgress.start();
		$.getJSON(window.baseUrl+'/path.html?path=' + path, function(json) {
			NProgress.done();
			if (ok(json)) {
				if (json.data) {
					var data = json.data;
					var childrens = data.childrens;
					
					parentNode.data = data;
					
					// Setting childrens
					if (appendNodes) {
						var zNodes = convertNodes(childrens);
						zTree.removeChildNodes(parentNode);
						if (zNodes.length > 0) {
							zTree.addNodes(parentNode, zNodes);
						}
					}
				}
				parentNode.isLoaded = true;
				if (callback) callback();
			}
		});
	}
	
	function deletePath(path, callback) {
		NProgress.start();
		$.getJSON(window.baseUrl+'/delete.html?path=' + path, function(json) {
			NProgress.done();
			if (ok(json)) {
				callback(path);
			}
			else {
				alert('Delete node ' + path + ' failed! ' + json.fail);
			}
		});
	}
	
	function savePath(path, data, callback) {
		NProgress.start();
		$.post(window.baseUrl+'/save.html', {
			path : path,
			data : data
		}, function(json) {
			NProgress.done();
			if (ok(json)) {
				callback(path, data);
			}
			else {
				alert('Save node ' + path + ' failed! ' + json.fail);
			}
		}, "json");
	}
	
	function convertNodes(childrens) {
		var zNodes = [];
		$.each(childrens, function(i, item) {
			var obj = convertPath(item);
			zNodes.push(obj);
		});
		return zNodes;
	}
	
	function convertPath(item) {
		var name = item.substring(item.lastIndexOf('/'), item.length)
		return {
			name : name, 
			open : true,
			path : item,
			iconSkin : "custom",
			isLoaded : false
		};
	}
	
	function ok(data) {
		return (data && data.statusCode == SUCCESS) ;
	}
	
	$(function() {
		node.loadDefault();
	});
	
})(jQuery);