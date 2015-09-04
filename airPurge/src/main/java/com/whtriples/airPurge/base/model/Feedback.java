package com.whtriples.airPurge.base.model;

import java.util.Date;

import com.rps.util.dao.annotation.ColumnIgnore;
import com.rps.util.dao.annotation.GenerateByDb;
import com.rps.util.dao.annotation.Id;
import com.rps.util.dao.annotation.Table;

@Table("t_d_feedback")
public class Feedback {
	/** 反馈ID */
	@Id
	@GenerateByDb
	private Integer feedback_id;

	/** 居民 */
	private Integer user_id;

	/** 反馈内容 */
	private String content;

	/** 反馈时间 */
	private Date feedback_time;

	/** 备注 */
	private String remark;

	/** 当时版本号 */
	private String version;

	
	@ColumnIgnore
	private String user_name;
	
	public Integer getFeedback_id() {
		return feedback_id;
	}

	public void setFeedback_id(Integer feedback_id) {
		this.feedback_id = feedback_id;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getFeedback_time() {
		return feedback_time;
	}

	public void setFeedback_time(Date feedback_time) {
		this.feedback_time = feedback_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}


	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	
}