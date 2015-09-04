(ns sql.device_user
  (:use [com.rps.util.dao.cljutil.daoutil2] )
  (:import [javax.servlet.http HttpServletRequest]
           [com.rps.util.dao SqlAndParams]
           [java.util ArrayList]))

 (defsql getPageDeviceUser
  {
   :sql "select d.device_guid,d.remarks,u.user_name,du.device_authority,du.device_id,du.user_id from t_d_device_user du left join t_p_user u on du.user_id=u.USER_ID
         left join t_d_device d on du.device_id=d.device_id"
   :where (AND 
		   ("startDate"  "l.record_time >= ?")
		   ("endDate"  "l.record_time <= ?")
       "u.USER_TYPE='2' " 
	)
   :page true
   :orderby true
   :orderby-default " order by du.user_id desc "
   })