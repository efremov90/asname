package org.asname.integration.wsspring;

import java.util.Date;

public interface HumanResourceService {
    void bookHoliday(Date startDate, Date endDate, String name);
}