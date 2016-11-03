mainApp.controller('FormsCtrl', 
			['$scope', 'modalStatus', 'modalParam', '$uibModalInstance', 'GlobalParameter', '$sce', 'rootscopeService',
	function($scope, modalStatus, modalParam, $uibModalInstance, GlobalParameter, $sce, rootscopeService){
	$scope.status = modalStatus;
	$scope.parentScope = modalParam;
	$scope.cancel = function () {
		$uibModalInstance.close();
	};
	
	function FormDoc (description){
		this.initBaseAddress('/forms/');
		this.description = description;
		return this;
	}
	FormDoc.prototype.initBaseAddress = function(baseAddress){
		this.baseAddress = baseAddress;
		return this;
	}
	FormDoc.prototype.initDoc = function (fileName, fileExt, icon, iconBgClass){
		if(fileName != null) {
			this.fileName = fileName;
		} else {
			this.fileName = this.description;
		}
		this.fileExt = fileExt;
		this.icon = icon;
		this.iconBgClass = iconBgClass;
		return this;
	}
	FormDoc.prototype.initLabel = function (label, labelClass){
		this.label = label;
		this.labelClass = labelClass;
		return this;
	}
	FormDoc.prototype.initDate = function(releaseDate){
		this.releaseDate = releaseDate;
		return this;
	}

	$scope.formPanelArray = [
							{
								title: 'Transit',
								class: 'col-md-7',
								forms:[
									new FormDoc('Tender Award Confirmation')
										.initDoc(null, '.doc', 'fa fa-file-word-o', 'icon bg-success-light')
										.initLabel('New', 'label-danger')
										.initDate('2016-11-07'),
									new FormDoc('Transit Verification Confirmation')
										.initDoc(null, '.doc', 'fa fa-file-word-o', 'icon bg-info-light')
										.initLabel('New', 'label-danger')
										.initDate('2016-11-07'),
									new FormDoc('New Project BQ Master Confirmation')
										.initDoc(null, '.doc', 'fa fa-file-word-o', 'icon bg-warning-light')
										.initLabel('New', 'label-danger')
										.initDate('2016-11-07'),
									new FormDoc('QS 2.0 Transit Template')
										.initDoc(null, '.xlsx', 'fa fa-file-excel-o', 'icon bg-danger-light')
										.initLabel('New', 'label-danger')
										.initDate('2016-11-07'),
									new FormDoc('QS 2.0 Transit Sample')
										.initDoc(null, '.xlsx', 'fa fa-file-excel-o', 'icon bg-grey-light')
										.initLabel('New', 'label-danger')
										.initDate('2016-11-07')
								]
							},
							{
								title: 'Services',
								class: 'col-md-5',
								forms:[
									new FormDoc('User Account Application')
										.initDoc('PDS_18%20Form%201%20User%20Account%20Administration%20Form', '.pdf', 'fa fa-file-word-o', 'icon bg-success-light')
										.initBaseAddress('http://gammon/BMS/Project%20Delivery/PDS%2018%20IMS%20Final%20Documents/'),
									new FormDoc('IMS Service')
										.initDoc('PDS_18_Form%202_IMS%20Service%20Request%20Form', '.pdf', 'fa fa-file-word-o', 'icon bg-success-light')
										.initBaseAddress('http://gammon/BMS/Project%20Delivery/PDS%2018%20IMS%20Final%20Documents/')				
								]
							}
						];
	

}]);