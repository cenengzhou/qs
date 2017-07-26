mainApp.controller('AdminRevisionsAddendumCtrl', ['$scope', 'modalService', 'GlobalHelper', 'GlobalParameter', 'addendumService',
										function($scope, modalService, GlobalHelper, GlobalParameter, addendumService) {

	Field.count = 0;
	Field.prototype.getNextOrder = fieldGetNextOrder;
	Field.prototype.setOrderNo = fieldSetOrderNo;
	Field.prototype.setOptions = fieldSetOptions;
	Field.prototype.setReadOnly = fieldSetReadOnly;
	Field.prototype.hide = fieldHide;
	$scope.addendum = {};
	$scope.addendumSearch = {};
	$scope.openDropdown = openDropdown;
	$scope.loadDatePickerFn = loadDatePickerFn;
	$scope.onAddendumSearch = onAddendumSearch;
	$scope.onAddendumUpdate = onAddendumUpdate;
	$scope.fieldList = [
		new Field('amtAddendum', 'Addendum Amount', 'number'),
		new Field('amtAddendumTotal', 'Addendum Total Amount', 'number'),
		new Field('amtAddendumTotalTba', 'Addendum Total TBA Amount', 'number'),
		new Field('amtSubcontractRemeasured', 'Subcontract Remesured Amount', 'number'),
		new Field('amtSubcontractRevised', 'Subcontract Revised Amount', 'number'),
		new Field('amtSubcontractRevisedTba', 'Subcontract Revised TBA Amount', 'number'),
		new Field('dateApproval', 'Approval Date', 'date'),
		new Field('dateCreated', 'Created Date', 'date').setReadOnly(true).hide(),
		new Field('dateLastModified', 'Last Modified Date', 'date').setReadOnly(true).hide(),
		new Field('dateSubmission', 'Submission Date', 'date'),
		new Field('descriptionSubcontract', 'Subcontract Description'),
		new Field('id', null, 'number').setReadOnly(true).hide(),
		new Field('idSubcontract', 'Subcontract Id', 'number').setReadOnly(true).hide(),
		new Field('nameSubcontractor', 'Subcontractor Name'),
		new Field('no', 'Addendum No.', 'number'),
		new Field('noAddendumDetailNext', 'Next Addendum Detail No.', 'number'),
		new Field('noJob', 'Job No.', 'number'),
		new Field('noSubcontract', 'Subcontract No.', 'number'),
		new Field('noSubcontractor', 'Subcontractor No.', 'number'),
		new Field('remarks', 'Remarks', 'textarea'),
		new Field('status', 'Status', 'select').setOptions([{id:'SUBMITTED', value:'Submitted'}, {id:'PENDING', value:'Pending'}, {id:'APPROVED', value:'Approved'}]),
		new Field('statusApproval', 'Approval Status', 'select').setOptions([{id:'NA', value:'N/A'}, {id:'APPROVED', value: 'Approved'}]),
		new Field('title', 'Title'),
		new Field('usernameCreated', 'Created User').setReadOnly(true).hide(),
		new Field('usernameLastModified', 'Last Modified User').setReadOnly(true).hide(),
		new Field('usernamePreparedBy', 'Prepared By')
	];
	
	function onAddendumSearch(){
		if(GlobalHelper.checkNull([$scope.addendumSearch.jobNo, $scope.addendumSearch.subcontractNo, $scope.addendumSearch.addendumNo])) return;
		addendumService.getAddendum($scope.addendumSearch.jobNo, $scope.addendumSearch.subcontractNo, $scope.addendumSearch.addendumNo)
		.then(function(data){
			if(data == "") {
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Warn', "Addendum not found");
				$scope.addendum = {};
			} else {
				$scope.addendum = data;
				$scope.addendum.noJob = parseInt($scope.addendum.noJob, 10);
				$scope.addendum.noSubcontract = parseInt($scope.addendum.noSubcontract, 10);
				$scope.addendum.noSubcontractor = parseInt($scope.addendum.noSubcontractor, 10);
			}
		})
	}
	
	function onAddendumUpdate(){
		addendumService.updateAddendum($scope.addendum)
		.then(function(data){
			if(data != ""){
				modalService.open('md', 'view/message-modal.html', 'MessageModalCtrl', 'Fail', data);
			} else {
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
		      $scope.addendum[ev.delegateTarget.name.replace(/.singleDate/, '')] = $(this).val();
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
