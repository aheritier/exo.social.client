/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.social.client.core.service;

import org.apache.http.HttpResponse;
import org.exoplatform.social.client.api.auth.AccessDeniedException;
import org.exoplatform.social.client.api.common.RealtimeListAccess;
import org.exoplatform.social.client.api.model.RestActivity;
import org.exoplatform.social.client.api.model.RestComment;
import org.exoplatform.social.client.api.model.RestIdentity;
import org.exoplatform.social.client.api.model.RestLike;
import org.exoplatform.social.client.api.net.SocialHttpClient.POLICY;
import org.exoplatform.social.client.api.service.ActivityService;
import org.exoplatform.social.client.api.service.ServiceException;
import org.exoplatform.social.client.core.model.RestCommentImpl;
import org.exoplatform.social.client.core.model.RestLikeImpl;
import org.exoplatform.social.client.core.util.SocialHttpClientSupport;
import org.exoplatform.social.client.core.util.SocialJSONDecodingSupport;
import org.exoplatform.social.client.core.model.RestActivityImpl;
import org.exoplatform.social.client.core.model.ActivitiesRealtimeListAccess;
import org.exoplatform.social.client.core.model.ActivitiesRealtimeListAccess.ActivityType;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Implementation of {@link ActivityService}.
 *
 * @author <a href="http://hoatle.net">hoatle (hoatlevan at gmail dot com)</a>
 * @since Jun 28, 2011
 */
public class ActivityServiceImpl extends ServiceBase<RestActivity, ActivityService<RestActivity>> implements ActivityService<RestActivity> {
  private static final String BASE_URL = SocialHttpClientSupport.buildCommonRestPathFromContext(true);

  /**
   * {@inheritDoc}
   */
  @Override
  public RestActivity create(RestActivity newInstance) throws AccessDeniedException, ServiceException {
    final String POST_ACTIVITY_REQUEST_URL = BASE_URL+"activity.json";
      try{
        HttpResponse response = SocialHttpClientSupport.executePost(POST_ACTIVITY_REQUEST_URL, POLICY.BASIC_AUTH,newInstance);
        String responseContent = SocialHttpClientSupport.getContent(response);
        RestActivity restActivity = SocialJSONDecodingSupport.parser(RestActivityImpl.class, responseContent);
        return restActivity;
      } catch (Exception e) {
        throw new ServiceException(ActivityServiceImpl.class,e.getMessage(),null);
      }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RestActivity get(String uuid) throws AccessDeniedException, ServiceException {
    final String GET_ACTIVITY_REQUEST_URL = BASE_URL+"activity/"+uuid+".json";
      try{
        HttpResponse response = SocialHttpClientSupport.executeGet(GET_ACTIVITY_REQUEST_URL, POLICY.BASIC_AUTH);
        String responseContent = SocialHttpClientSupport.getContent(response);
        RestActivity restActivity = SocialJSONDecodingSupport.parser(RestActivityImpl.class, responseContent);
        return restActivity;
      } catch (Exception e) {
        throw new ServiceException(ActivityServiceImpl.class,e.getMessage(),null);
      }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RestActivity update(RestActivity existingInstance) throws AccessDeniedException, ServiceException {
    throw new ServiceException(ActivityServiceImpl.class,"Do Not Support",null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RestActivity delete(RestActivity existingInstance) throws AccessDeniedException, ServiceException {
    final String DELETE_ACTIVITY_REQUEST_URL = BASE_URL+"activity/destroy/"+existingInstance.getId()+".json";
    try{
      HttpResponse response = SocialHttpClientSupport.executePost(DELETE_ACTIVITY_REQUEST_URL,POLICY.BASIC_AUTH);
      String responseContent = SocialHttpClientSupport.getContent(response);
      RestActivity restActivity = SocialJSONDecodingSupport.parser(RestActivityImpl.class, responseContent);
      return restActivity;
    } catch (Exception e) {
      throw new ServiceException(ActivityServiceImpl.class,e.getMessage(),null);
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public RealtimeListAccess<RestActivity> getActivityStream(RestIdentity restIdentity) throws AccessDeniedException,
                                                                          ServiceException {
    return new ActivitiesRealtimeListAccess(restIdentity, ActivityType.ACTIVITY_STREAM);
  }

  @Override
  public RealtimeListAccess<RestActivity> getSpacesActivityStream(RestIdentity userRestIdentity) throws AccessDeniedException, ServiceException {
    return new ActivitiesRealtimeListAccess(userRestIdentity, ActivityType.USER_SPACE_ACTIVITIES);
  }

  @Override
  public RealtimeListAccess<RestActivity> getConnectionsActivityStream(RestIdentity userRestIdentity) throws AccessDeniedException,
                                                                                                 ServiceException {
    return new ActivitiesRealtimeListAccess(userRestIdentity, ActivityType.CONNECTIONS_ACTIVITIES);
  }

  @Override
  public RealtimeListAccess<RestActivity> getFeedActivityStream(RestIdentity userRestIdentity) throws AccessDeniedException,
                                                                                          ServiceException {
    return new ActivitiesRealtimeListAccess(userRestIdentity, ActivityType.ACTIVITY_FEED);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public RestComment createComment(RestActivity existingRestActivity, RestComment newRestComment)
                                                                             throws  AccessDeniedException,
                                                                                     ServiceException {
    final String CREATE_COMMENT_REQUEST_URL = BASE_URL+"activity/"+ existingRestActivity.getId()+"/comment.json";
      try{
        HttpResponse response = SocialHttpClientSupport.executePost(CREATE_COMMENT_REQUEST_URL,POLICY.BASIC_AUTH, newRestComment);
        String responseContent = SocialHttpClientSupport.getContent(response);
        RestComment restComment = SocialJSONDecodingSupport.parser(RestCommentImpl.class, responseContent);
        return restComment;
      } catch (Exception e) {
        throw new ServiceException(ActivityServiceImpl.class,e.getMessage(),null);
      }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RestComment getComment(String commentId) throws AccessDeniedException, ServiceException {
    throw new ServiceException(ActivityServiceImpl.class, "Not Supported",null);
//    final String GET_ACTIVITY_REQUEST_URL = BASE_URL+commentId+".json";
//    try {
//      HttpResponse response = SocialHttpClientSupport.executeGet(GET_ACTIVITY_REQUEST_URL,POLICY.BASIC_AUTH);
//      int statusCode = response.getStatusLine().getStatusCode();
//      if(statusCode != ServiceException.HTTP_OK){
//          throw new ServiceException(ActivityServiceImpl.class,"invalid response: Status " + statusCode, null);
//      } else {
//        String responseContent = SocialHttpClientSupport.getContent(response);
//        try{
//          RestComment comment = SocialJSONDecodingSupport.parser(RestComment.class, responseContent);
//          return comment;
//        } catch (Exception e) {
//          throw new ServiceException(ActivityServiceImpl.class,"invalid response",null);
//        }
//      }
//    } catch (Exception e) {
//      throw new ServiceException(ActivityServiceImpl.class, "There's error when execute request",null);
//    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RestComment deleteComment(RestComment existingRestComment) throws AccessDeniedException,
          ServiceException {
    final String DELETE_COMMENT_REQUEST_URL = BASE_URL+"activity/"+ existingRestComment.getActivityId() + "/comment/destroy/" +
                                            existingRestComment.getId() + ".json";
    try{
      HttpResponse response = SocialHttpClientSupport.executePost(DELETE_COMMENT_REQUEST_URL,POLICY.BASIC_AUTH);
      String responseContent = SocialHttpClientSupport.getContent(response);
      RestComment restComment = SocialJSONDecodingSupport.parser(RestCommentImpl.class, responseContent);
      return restComment;
    } catch (Exception e) {
      throw new ServiceException(ActivityServiceImpl.class,"invalid response",null);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RestLike like(RestActivity existingRestActivity) throws AccessDeniedException, ServiceException {
    final String LIKE_ACTIVITY_REQUEST_URL = BASE_URL+"activity/"+ existingRestActivity.getId()+"/like.json";
    HttpResponse response = SocialHttpClientSupport.executePost(LIKE_ACTIVITY_REQUEST_URL,POLICY.BASIC_AUTH);
    String responseContent = SocialHttpClientSupport.getContent(response);
    try{
      JSONObject responeJson = (JSONObject)JSONValue.parse(responseContent);
      if((Boolean) responeJson.get("like")){
        return new RestLikeImpl(existingRestActivity.getId(), null);
      } else {
        throw new ServiceException(ActivityServiceImpl.class,"invalid response",null);
      }
    } catch (Exception e) {
      throw new ServiceException(ActivityServiceImpl.class,"invalid response",null);
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public RestLike unlike(RestActivity existingRestActivity) throws AccessDeniedException, ServiceException {
    final String LIKE_ACTIVITY_REQUEST_URL = BASE_URL+"activity/"+ existingRestActivity.getId()+"/like/destroy.json";
    HttpResponse response = SocialHttpClientSupport.executePost(LIKE_ACTIVITY_REQUEST_URL,POLICY.BASIC_AUTH);
    String responseContent = SocialHttpClientSupport.getContent(response);
    JSONObject responeJson = (JSONObject)JSONValue.parse(responseContent);
    if(!(Boolean) responeJson.get("like")){
      return new RestLikeImpl(existingRestActivity.getId(), null);
    } else {
      throw new ServiceException(ActivityServiceImpl.class,"invalid response",null);
    }
  }
}
