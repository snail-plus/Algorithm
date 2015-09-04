(ns sql.feedback
  (:use [com.rps.util.dao.cljutil.daoutil2] )
  (:import [javax.servlet.http HttpServletRequest]
           [com.rps.util.dao SqlAndParams]
           [java.util ArrayList]))

(defsql getPageFeedback {
   :sql "SELECT
	         f.*, u.user_name
         FROM
	         t_d_feedback f
         LEFT JOIN t_p_user u ON u.user_ID = f.user_id"
   :where (AND
       ("start_time"  "feedback_time >= ?")
		   ("end_time"  "feedback_time <= ?")
   )
   :page true
   :orderby true
   :orderby-default " ORDER BY feedback_time DESC"
})