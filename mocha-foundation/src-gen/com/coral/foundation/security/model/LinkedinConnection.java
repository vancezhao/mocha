package com.coral.foundation.security.model;
import java.util.*;
import java.math.BigDecimal;
import javax.persistence.*;
import com.coral.foundation.model.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
  * <p>Title: com.coral.foundation.security.model.LinkedinConnection + "</p>
  * <p>Description: The LinkedinConnection entity </p>
  */
@Entity(name = "LinkedinConnection")
@Table(name = "T_LINKEDIN_CONNECTION")
public class LinkedinConnection extends BaseEntity {
	
	@Id()
	@Column (name = "LINKEDIN_CONNECTION_ID")
	@GeneratedValue(strategy = GenerationType. AUTO)
	private Long linkedinConnectionId;
	
	@Basic(optional = true)
	@Column(name = "FIRST_NAME" )
	private String firstName;
	
	
	@Basic(optional = true)
	@Column(name = "LAST_NAME" )
	private String lastName;
	
	
	@Basic(optional = true)
	@Column(name = "COMPANY_NAME" )
	private String companyName;
	
	
	@Basic(optional = true)
	@Column(name = "HEADLINE" )
	private String headline;
	
	
	@Basic(optional = true)
	@Column(name = "CURRENT_COMPNAY" )
	private String currentCompnay;
	
	
	@Basic(optional = true)
	@Column(name = "PICT_URL" )
	private String pictUrl;
	
	
	@Basic(optional = true)
	@Column(name = "LAST_UPDATE_DATE" )
	@Temporal(TemporalType.DATE)
	private Date lastUpdateDate;
	
	@Basic(optional = true)
	@Column(name = "NEED_FOLLOW" )
	private Boolean needFollow;
	
	
	@Basic(optional = true)
	@Column(name = "LOCATION" )
	private String location;
	
	
	@Basic(optional = true)
	@Column(name = "LOCATION_COUNTRY" )
	private String locationCountry;
	
	
	@Basic(optional = true)
	@Column(name = "SUMMARY" )
	private String summary;
	
	
	@Basic(optional = true)
	@Column(name = "CURRENT_STATUS" )
	private String currentStatus;
	
	
	@Basic(optional = true)
	@Column(name = "IM_ACCOUNT" )
	private String imAccount;
	
	
	@Basic(optional = true)
	@Column(name = "TWITTER_ACCOUNT" )
	private String twitterAccount;
	
	
	@Basic(optional = true)
	@Column(name = "EMAIL_ADDRESS" )
	private String emailAddress;
	
	
	@Basic(optional = true)
	@Column(name = "INDUSTRY" )
	private String industry;
	
	
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH }, targetEntity = LinkedinPersonProfile.class, fetch=FetchType.EAGER)
	@JoinColumns({ @JoinColumn(name = "linkedinPersonProfile") })
	@Fetch(FetchMode.JOIN)
	private LinkedinPersonProfile linkedinPersonProfile;
	
	@OneToMany(cascade = { CascadeType.ALL }, targetEntity = LinkedinGroup.class, fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinColumn(name="LINKEDIN_CONNECTION_ID")
	private List<LinkedinGroup> linkedinGroups = new ArrayList<LinkedinGroup>();
	
	@OneToMany(cascade = { CascadeType.ALL }, targetEntity = LinkedinConnectionNetworkUpdate.class, fetch=FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinColumn(name="LINKEDIN_CONNECTION_ID")
	private List<LinkedinConnectionNetworkUpdate> linkedinConnectionNetworkUpdate = new ArrayList<LinkedinConnectionNetworkUpdate>();
	

	public void setLinkedinConnectionId (Long linkedinConnectionId) {
		this.linkedinConnectionId = linkedinConnectionId;
	} 
	public Long getLinkedinConnectionId () {
		return linkedinConnectionId;
	}
	public void setFirstName (String firstName) {
		this.firstName = firstName;
	} 
	public String getFirstName () {
		return firstName;
	}
	public void setLastName (String lastName) {
		this.lastName = lastName;
	} 
	public String getLastName () {
		return lastName;
	}
	public void setCompanyName (String companyName) {
		this.companyName = companyName;
	} 
	public String getCompanyName () {
		return companyName;
	}
	public void setHeadline (String headline) {
		this.headline = headline;
	} 
	public String getHeadline () {
		return headline;
	}
	public void setCurrentCompnay (String currentCompnay) {
		this.currentCompnay = currentCompnay;
	} 
	public String getCurrentCompnay () {
		return currentCompnay;
	}
	public void setPictUrl (String pictUrl) {
		this.pictUrl = pictUrl;
	} 
	public String getPictUrl () {
		return pictUrl;
	}
	public void setLastUpdateDate (Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	} 
	public Date getLastUpdateDate () {
		return lastUpdateDate;
	}
	public void setNeedFollow (Boolean needFollow) {
		this.needFollow = needFollow;
	} 
	public Boolean getNeedFollow () {
		return needFollow;
	}
	public void setLocation (String location) {
		this.location = location;
	} 
	public String getLocation () {
		return location;
	}
	public void setLocationCountry (String locationCountry) {
		this.locationCountry = locationCountry;
	} 
	public String getLocationCountry () {
		return locationCountry;
	}
	public void setSummary (String summary) {
		this.summary = summary;
	} 
	public String getSummary () {
		return summary;
	}
	public void setCurrentStatus (String currentStatus) {
		this.currentStatus = currentStatus;
	} 
	public String getCurrentStatus () {
		return currentStatus;
	}
	public void setImAccount (String imAccount) {
		this.imAccount = imAccount;
	} 
	public String getImAccount () {
		return imAccount;
	}
	public void setTwitterAccount (String twitterAccount) {
		this.twitterAccount = twitterAccount;
	} 
	public String getTwitterAccount () {
		return twitterAccount;
	}
	public void setEmailAddress (String emailAddress) {
		this.emailAddress = emailAddress;
	} 
	public String getEmailAddress () {
		return emailAddress;
	}
	public void setIndustry (String industry) {
		this.industry = industry;
	} 
	public String getIndustry () {
		return industry;
	}
	public void setLinkedinPersonProfile (LinkedinPersonProfile linkedinPersonProfile) {
		this.linkedinPersonProfile = linkedinPersonProfile;
	} 
	public LinkedinPersonProfile getLinkedinPersonProfile () {
		return linkedinPersonProfile;
	}
	public void setLinkedinGroups (List<LinkedinGroup> linkedinGroups) {
		this.linkedinGroups = linkedinGroups;
	} 
	public List<LinkedinGroup> getLinkedinGroups () {
		return linkedinGroups;
	}
	public void setLinkedinConnectionNetworkUpdate (List<LinkedinConnectionNetworkUpdate> linkedinConnectionNetworkUpdate) {
		this.linkedinConnectionNetworkUpdate = linkedinConnectionNetworkUpdate;
	} 
	public List<LinkedinConnectionNetworkUpdate> getLinkedinConnectionNetworkUpdate () {
		return linkedinConnectionNetworkUpdate;
	}

	public Long getID() {
		return getLinkedinConnectionId();
	}
	
	public void setID(Long id) {
		setLinkedinConnectionId(id);
	}
}
