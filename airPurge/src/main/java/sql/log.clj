(ns sql.log
  (:use [com.rps.util.dao.cljutil.daoutil2] )
  (:import [javax.servlet.http HttpServletRequest]
           [com.rps.util.dao SqlAndParams]
           [java.util ArrayList]))

 (defsql getPageLog
  {
   :sql "select d.remarks,l.*,u.user_name from t_d_log l left join t_p_user u on l.user_id=u.user_id
         left join t_d_device d on l.Device_GUID=d.Device_GUID"
   :where (AND 
		   ("startDate"  "l.record_time >= ?")
		   ("endDate"  "l.record_time <= ?")
       ("user_name" "u.user_name like ?" "%" "%")
	)
   :page true
   :orderby true
   :orderby-default " order by l.log_id asc"
   })