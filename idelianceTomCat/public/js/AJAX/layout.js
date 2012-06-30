$(document).ready(
		function() {
			$('#addCategorieButton').click(function() {
				var categoryEntitled = $('#addCategorieInput').val();
				$.ajax({
					type : "POST",
					url : "/category/add",
					data : "categoryEntitled=" + categoryEntitled,
					success : function(e) {
						$('#addCategorie').modal('hide');

					},
					error : function(e) {
						$('#addCategorie').modal('hide');
					}
				});
				
				location.reload();
				
				return false;
			});
			$('#addRelationButton').click(
				function() {
					var entitled1 = $('#addRelationEntitled1').val();
					var entitled2 = $('#addRelationEntitled2').val();
					$.ajax({
						type : "POST",
						url : "/relation/add",
						data : "entitled1=" + entitled1 + "&entitled2="
								+ entitled2 + "&ajax=true",
						success : function(e) {
							$('#addRelation').modal('hide');
						},
						error : function(e) {
							$('#addRelation').modal('hide');
						}
					});
					
					location.reload();
					
					return false;
				});

			$("#table-sidebar").tablesorter({
				sortList : [ [ 0, 0 ] ]
			});

			$("#table-category").tablesorter({
				sortList : [ [ 1, 0 ] ],
				headers : {
					0 : {
						sorter : false
					},
					2 : {
						sorter : false
					}
				}
			});

			$("#table-subject-list").tablesorter({
				sortList : [ [1, 0] ],
				headers : {
					0 : {
						sorter : false
					},
					2 : {
						sorter : false
					}
				}
			});

			$("#table-subject-search").tablesorter({
				sortList : [ [0, 0] ],
				headers : {
					1 : {
						sorter : false
					}
				}
			});

			$("#table-relation-search").tablesorter({
				sortList : [ [0, 0] ],
				headers : {
					1 : {
						sorter : false
					}
				}
			});

			$("#table-subject").tablesorter({
				sortList : [ [ 0, 0 ], [1, 0] ],
				headers : {
					2 : {
						sorter : false
					}
				}
			});

			$("#table-relation").tablesorter({
				sortList : [ [ 0, 0 ], [2, 0] ],
				headers : {
					1 : {
						sorter : false
					},
					3 : {
						sorter : false
					}
				}
			});

			$("#table-triplet").tablesorter({
				sortList : [ [ 2, 0 ], [ 3, 0 ], [4, 0] ],
				headers : {
					0 : {
						sorter : false
					},
					5 : {
						sorter : false
					}
				}
			});

			$('a[rel=tooltip]').tooltip().click(function(e) {
				e.preventDefault()
			})

			// popover demo
			$("a[rel=popover]").popover({
				placement : 'top'
			}).click(function(e) {
				e.preventDefault()
			})
		});