var Modal = {
	attr: {
		id : 'modal',
		label : 'modalLabel'
	},
	tags: {
		modal: null, 
		modal_dialog: null, 
		modal_content: null, 
		modal_header: null, 
		modal_header_close: null, 
		modal_header_title: null, 
		modal_body: null, 
		modal_footer: null, 
		modal_footer_close: null, 
		modal_footer_ok: null
	},
	init: function() {
		this.build();
	},
	
	modal: function() {
		return $('#' + this.attr.id);
	},
	
	/**
	 * options: {
	 *     title: String
	 *     body: Document
	 *     backdrop: Boolean
	 *     shown: function
	 *     hiden: function
	 *     closeHandler: function
	 *     okHandler: function
	 *     size: lg|sm,
	 *     okBtn: show|hide,
	 *     closeBtn: show|hide,
	 *     headerCloseBtn: show|hide,
	 *     keyboard: true|false,
	 *     btns : {
	 *       ok : {
	 *         text : 'Confirm',
	 *         onClick : Function
	 *       },
	 *       close : {
	 *         text : 'Close'
	 *       }
	 *     }
	 * }
	 */
	open: function(options) {
		this.empty();
		var $this = this;
		if (options) {
			var title = options.title;
			var body = options.body;
			var close = options.closeHandler;
			var ok = options.okHandler;
			var size = options.size;
			
			if (title) {
				this.tags.modal_header_title.text(title);
			}
			if (body) {
				this.tags.modal_body.append(body);
			}
			if (close) {
				this.tags.modal_footer_close.bind('click', close);
			}
			if (ok) {
				this.tags.modal_footer_ok.bind('click', ok);
			}
			if (size) {
				this.tags.modal_dialog.addClass((size == 'lg') ? 'modal-lg' : 'modal-sm');
			}
			
			this.modal().bind('shown.bs.modal', function() {
				if (options.shown) options.shown($this);
			});
			this.modal().bind('hidden.bs.modal', function() {
				if (options.hiden) options.hiden($this);
			});
			if (options.backdrop)
				this.modal().data('backdrop', options.backdrop);
			if (typeof(options.keyboard) == 'undefined')
				options.keyboard = true;
			this.modal().modal({
				keyboard : (typeof(options.keyboard) == 'undefined') || options.keyboard
			});
		}
		
		this.tags.modal_footer_ok.removeClass('hide');
		this.tags.modal_footer_close.removeClass('hide');
		this.tags.modal_header_close.removeClass('hide');
		if (options) {
			if (options.okBtn && options.okBtn == 'hide') {
				this.hideOkButton();
			}
			if (options.closeBtn && options.closeBtn == 'hide') {
				this.hideCloseButton();
			}
			if (options.headerCloseBtn && options.headerCloseBtn == 'hide') {
				this.hideHeaderCloseButton();
			}
			
			var btns = options.btns;
			if (btns.ok) {
				if (btns.ok.text) {
					this.tags.modal_footer_ok.text(btns.ok.text);
				}
				if (btns.ok.onClick) {
					this.tags.modal_footer_ok.bind('click', function(e) {
						btns.ok.onClick(e, $this);
					});
				}
			}
			if (btns.close) {
				if (btns.close.text) {
					this.tags.modal_footer_close.text(btns.close.text);
				}
			}
		}
	},
	
	close: function(callback) {
		this.modal().modal('hide');
		this.modal().on('hidden.bs.modal', function(e) {
			if (callback) callback();
		});
	},
	
	empty: function() {
		this.tags.modal_header_title.text('');
		this.tags.modal_body.empty();
		this.tags.modal_footer_close.unbind('click');
		this.tags.modal_footer_ok.unbind('click');
		this.modal().unbind('shown.bs.modal');
		this.modal().unbind('hidden.bs.modal');
		this.tags.modal_dialog.removeClass('modal-lg modal-sm');
	},
	
	hideOkButton: function() {
		this.tags.modal_footer_ok.addClass('hide');
	},
	hideCloseButton: function() {
		this.tags.modal_footer_close.addClass('hide');
	},
	hideHeaderCloseButton: function() {
		this.tags.modal_header_close.addClass('hide');
	},
	
	build: function() {
		if (this.modal().length > 0) {
			return;
		}
		
		this.tags.modal = $('<div class="modal fade" id="' + this.attr.id + '" tabindex="-1" role="dialog" aria-labelledby="' + this.attr.label + '" aria-hidden="false" data-backdrop="false">');
		this.tags.modal_dialog = $('<div class="modal-dialog">');
		this.tags.modal_content = $('<div class="modal-content">');
		this.tags.modal_header = $('<div class="modal-header">');
		this.tags.modal_header_close = $('<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>');
		this.tags.modal_header_title = $('<h4 class="modal-title" id="' + this.attr.label + '"></h4>');
		this.tags.modal_body = $('<div class="modal-body">');
		this.tags.modal_footer = $('<div class="modal-footer">');
		this.tags.modal_footer_close = $('<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>');
		this.tags.modal_footer_ok = $('<button type="submit" class="btn btn-primary">确定</button>');
		
		this.tags.modal_footer.append(this.tags.modal_footer_close);
		this.tags.modal_footer.append(this.tags.modal_footer_ok);
		
		this.tags.modal_header.append(this.tags.modal_header_close);
		this.tags.modal_header.append(this.tags.modal_header_title);
		
		this.tags.modal_content.append(this.tags.modal_header);
		this.tags.modal_content.append(this.tags.modal_body);
		this.tags.modal_content.append(this.tags.modal_footer);
		
		this.tags.modal_dialog.append(this.tags.modal_content);
		
		this.tags.modal.append(this.tags.modal_dialog);
		
		$('body').append(this.tags.modal);
	}
};
$(function() {Modal.init();});