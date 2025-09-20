class NotificationService {
  constructor() {
    this.notificationCallback = null;
  }

  setNotificationCallback(callback) {
    this.notificationCallback = callback;
  }

  showNotification(message, type = 'info') {
    if (this.notificationCallback) {
      this.notificationCallback(message, type);
    } else {
      console.log(`Notification: ${message} (${type})`);
    }
  }
}

const notificationService = new NotificationService();
export default notificationService;
