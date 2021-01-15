package uu.datamanagement.main.abl.entity;

import uu.datamanagement.main.utils.TimeInterval;

public interface EntityWithTimeInterval {

  TimeInterval getTimeInterval();

  void setTimeInterval(TimeInterval timeInterval);
}
