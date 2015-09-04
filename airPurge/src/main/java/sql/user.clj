(ns sql.user
  (:use [com.rps.util.dao.cljutil.daoutil2] )
  (:import [javax.servlet.http HttpServletRequest]
           [com.rps.util.dao SqlAndParams]
           [java.util ArrayList]))

(def getUserByLoginID
   "select * from T_P_USER where LOGIN_ID = ?"
  )
 
(def getUserById
   "select * from T_P_USER where USER_ID = ?"
  )

 (defsql getPageUser
  {
   :sql "select * from T_P_USER "
   :where (AND 
		   ("status"  "status = ?")
		   ("login_id"  "login_id like ?" "%" "%")
		   ("user_name"  " user_name like ?" "%" "%")
	)
   :page true
   :orderby true
   :orderby-default " order by user_id "
   })