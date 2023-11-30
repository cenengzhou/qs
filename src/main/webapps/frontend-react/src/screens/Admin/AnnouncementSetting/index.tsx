import './style.css'

const AnnouncementSetting = () => {
  return (
    <div className="control-pane">
      <div className="card-wrapper">
        <div className="e-card-header">
          <div className="e-card-header-image bell"></div>
          <div className="e-card-header-caption">
            <div className="e-card-header-title"> Announcement Setting</div>
          </div>
        </div>
        <div className="e-card-footer">
          <small className="e-card-footer-title">
            Update Announcement Setting for ALL users.
          </small>
          <div className="e-card-footer-content">
            <div className="col-xs-6 col-sm-6 col-lg-4 col-md-4">
              <div className="e-card-footer-content-left">
                <div className="e-card-footer-content-button on">
                  <span className="e-icons e-audio"></span>
                  <b>Swtich ON</b>
                </div>
                <span className="e-card-footer-subtitle">
                  Announcement pop up will be shown.
                </span>
              </div>
            </div>
            <div className="col-xs-6 col-sm-6 col-lg-4 col-md-4">
              <div className="e-card-footer-content-right">
                <div className="e-card-footer-content-button off">
                  <span className="e-icons e-audio"></span>
                  <b>Swtich OFF</b>
                </div>
                <span>Announcement pop up will be hidden.</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default AnnouncementSetting
