databaseChangeLog = {
    include file: 'rebased_db_initialisation.groovy'
    include file: 'missing_indices.groovy'
    include file: 'foreign_key_indices.groovy'
    include file: 'correct_url_in_detection_count_per_station.groovy'
    include file: 'truncate_instead_of_delete_on_refresh_matview.groovy'
    include file: 'add_project_name_index.groovy'
    include file: 'trim_serial_numbers.groovy'
    include file: '20140827-AS-AddUnavailabilityNotification.groovy'
    include file: 'matviews_entries.groovy'
    include file: 'drop_detection_surgery.groovy'
    include file: 'detection_query_optimisation.groovy'
    include file: 'remove_detection_matview.groovy'
}
