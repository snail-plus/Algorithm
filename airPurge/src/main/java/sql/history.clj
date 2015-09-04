(ns sql.history
  (:use [com.rps.util.dao.cljutil.daoutil2] )
  (:import [javax.servlet.http HttpServletRequest]
           [com.rps.util.dao SqlAndParams]
           [java.util ArrayList]))

 (defsql getPageHistory
  {
   :sql "select t.*,d.remarks from t_d_transducer t left join  t_d_device d on t.device_guid=d.device_guid"
   :where (AND 
		   ("startDate"  "t.record_time >= ?")
		   ("endDate"  "t.record_time <= ?")
       ("device_guid" "d.device_guid=?")
	)
   :page true
   :orderby true
   :orderby-default " order by record_time desc "
   })