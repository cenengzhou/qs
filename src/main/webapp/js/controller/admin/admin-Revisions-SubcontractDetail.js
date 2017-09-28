mainApp.controller('AdminRevisionsSubcontractDetailCtrl', ['$scope', 'modalService', 'GlobalHelper', 'GlobalParameter', 'subcontractService',
										function($scope, modalService, GlobalHelper, GlobalParameter, subcontractService) {

	Field.count = 0;
	Field.prototype.getNextOrder = fieldGetNextOrder;
	Field.prototype.setOrderNo = fieldSetOrderNo;
	Field.prototype.setOptions = fieldSetOptions;
	Field.prototype.setReadOnly = fieldSetReadOnly;
	Field.prototype.hide = fieldHide;
	$scope.subcontractDetail = {};
	$scope.subcontractDetailSearch = {};
	$scope.openDropdown = openDropdown;
	$scope.loadDatePickerFn = loadDatePickerFn;
	$scope.onSubcontractDetailSearch = onSubcontractDetailSearch;
	$scope.onSubcontractDetailUpdate = onSubcontractDetailUpdate;
	$scope.fieldList = [
		new Field('id', null, 'number').setReadOnly(true).hide(),
		new Field('lastModifiedDate', 'Last Modify Date', 'date').setReadOnly(true),
		new Field('lastModifiedUser', 'Last Modify User', 'text').setReadOnly(true),
		new Field('createdDate', 'Date Create', 'date').setReadOnly(true),
		new Field('createdUser', 'User Create', 'text').setReadOnly(true),
		new Field('systemStatus', 'System Status', 'select').setOptions([{id:'ACTIVE', value:'ACTIVE'}, {id:'INACTIVE', value:'INACTIVE'}]),
		new Field('jobNo', 'Job Number', 'text').setReadOnly(true),
		new Field('sequenceNo', 'Sequence Number', 'text').setReadOnly(true),
		new Field('resourceNo', 'Resource Number', 'text'),
		new Field('billItem', 'Bill Item', 'text'),
		new Field('description', 'Description', 'text'),
		new Field('quantity', 'Quantity', 'number'),
		new Field('scRate', 'Subcontract Rate', 'number'),
		new Field('objectCode', 'Object Code', 'text'),
		new Field('subsidiaryCode', 'Subsidiary Code', 'text'),
		new Field('lineType', 'Line Type', 'text'),
		new Field('approved', 'Approved', 'select').setOptions([{id:'A', value:'Approved'}, {id:'N', value:'Not Approved'}]),
		new Field('unit', 'Unit', 'text'),
		new Field('remark', 'Remark', 'textarea'),
		new Field('postedCertifiedQuantity', 'Posted Cert Qty', 'number'),
		new Field('cumCertifiedQuantity', 'Cum Cert Qty', 'number'),
		new Field('originalQuantity', 'Original Qty', 'number'),
		new Field('newQuantity', 'New Qty', 'number'),
		new Field('toBeApprovedRate', 'To Be Approved Rate', 'number'),
		new Field('amountSubcontract', 'Subcontract Amount', 'number'),
		new Field('amountBudget', 'Budget Amount', 'number'),
		new Field('amountCumulativeCert', 'Cum Cert Amount', 'number'),
		new Field('amountPostedCert', 'Posted Cert Amount', 'number'),
		new Field('amountSubcontractNew', 'New Subcontract Amount', 'number')
	];
	
	function onSubcontractDetailSearch(){
		if(GlobalHelper.checkNull([$scope.subcontractDetailSearch.jobNo, $scope.subcontractDetailSearch.subcontractNo, $scope.subcontractDetailSearch.sequenceNo])) return;
		subcontractService.getSubcontractDetail($scope.subcontractDetailSearch.jobNo, $scope.subcontractDetailSearch.subcontractNo, $scope.subcontractDetailSearch.sequenceNo)
		.then(function(data){
			if(data == "") {
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Subcontract Detail not found");
				$scope.subcontractDetail = {};
			} else {
				$scope.subcontractDetail = data;
			}
		})
	}
	
	function onSubcontractDetailUpdate(){
		subcontractService.updateSubcontractDetailAdmin($scope.subcontractDetail)
		.then(function(data){
			if(data === ""){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Success', "Record updated");
			}
		})
	}
	
	function openDropdown( $event){
		angular.element('input[name="' + $event.currentTarget.nextElementSibling.name + '"').click();
	}
	
	function loadDatePickerFn (){
		angular.element('input[name$=".singleDate"').daterangepicker({
		    singleDatePicker: true,
		    autoUpdateInput: false,
		    showDropdowns: true,
		    timePicker: false,
		    timePicker24Hour: true,
		    autoApply: true,
			locale: {
			      format: GlobalParameter.MOMENT_DATE_FORMAT
			    },

		});
		
		angular.element('input[name$=".singleDate"').on('apply.daterangepicker', function(ev, picker) {
		      $(this).val(picker.startDate.format(GlobalParameter.MOMENT_DATE_FORMAT));
		      $scope.subcontractDetail[ev.delegateTarget.name.replace(/.singleDate/, '')] = $(this).val();
		  });
		
		angular.element('input[name$=".singleDate"').on('cancel.daterangepicker', function(ev, picker) {
	      $(this).val('');
		});
	}

	function Field(name, label, type, readOnly, visible){
		this.name = name;
		this.label = label || name;
		this.type = type || 'text';
		this.order = this.getNextOrder();
		this.readOnly = readOnly || false;
		this.visible = visible || true;
		this.value = '';
		return this;
	}
	
	function fieldGetNextOrder(){
		Field.count += 1;
		return Field.count;
	}
	
	function fieldSetOrderNo(no){
		this.order = no;
		return this;
	}
	
	function fieldSetOptions(options){
		this.options = options;
		return this;
	}
	
	function fieldSetReadOnly(isReadOnly){
		this.readOnly = isReadOnly;
		return this;
	}
	
	function fieldHide(){
		this.visible = false;
		return this;
	}
	
}]);
