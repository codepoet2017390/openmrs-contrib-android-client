/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.mobile.activities.activevisits;

import org.openmrs.mobile.R;
import org.openmrs.mobile.dao.VisitDAO;
import org.openmrs.mobile.models.Visit;
import org.openmrs.mobile.utilities.FilterUtil;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;

public class ActiveVisitPresenter implements ActiveVisitsContract.Presenter{

    private ActiveVisitsContract.View mActiveVisitsView;
    private VisitDAO visitDAO;

    public ActiveVisitPresenter(ActiveVisitsContract.View mActiveVisitsView) {
        this.mActiveVisitsView = mActiveVisitsView;
        this.mActiveVisitsView.setPresenter(this);
        this.visitDAO = new VisitDAO();
    }

    @Override
    public void start() {
        updateVisitsInDatabaseList();
    }

    @Override
    public void updateVisitsInDatabaseList() {
        mActiveVisitsView.setEmptyListText(R.string.search_visits_no_results);
        visitDAO.getActiveVisits()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(visits -> {
                    mActiveVisitsView.updateListVisibility(visits);
                });
    }

    public void updateVisitsInDatabaseList(final String query) {
        mActiveVisitsView.setEmptyListText(R.string.search_patient_no_result_for_query, query);
        visitDAO.getActiveVisits()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(visits -> {
                    visits = FilterUtil.getPatientsWithActiveVisitsFilteredByQuery(visits, query);
                    mActiveVisitsView.updateListVisibility(visits);
                });
    }
}
