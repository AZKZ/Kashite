package com.azkz.application.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.azkz.businesslogic.service.FriendService;
import com.azkz.businesslogic.service.UserService;
import com.azkz.common.kashiteConst;
import com.azkz.infrastructure.entity.ViewFriendRelationList;
import com.azkz.infrastructure.entity.ViewUserFriendList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FriendController {

  private final UserService userService;
  private final FriendService friendService;

  @Autowired
  public FriendController(final UserService userService, FriendService friendService) {
    this.userService = userService;
    this.friendService = friendService;
  }

  /**
   * フレンドリストページへ遷移
   *
   * @param modelAndView
   * @param principal
   * @return
   */
  @GetMapping("/friend/list")
  public ModelAndView directFriendListPage(ModelAndView modelAndView,
      @AuthenticationPrincipal final OidcUser principal) {

    long userId = userService.getUserId(principal);

    // ユーザに紐づくフレンド一覧を取得
    List<ViewUserFriendList> viewUserFriendList = friendService.getFriendList(userId);

    // 未承認のリクエストの件数を取得
    int countAcceptionTarget = friendService.getCountAcceptionTarget(userId);

    modelAndView.addObject("viewUserFriendList", viewUserFriendList);
    modelAndView.addObject("countAcceptionTarget", countAcceptionTarget);
    modelAndView.setViewName("friendlist");

    return modelAndView;
  }

  /**
   * QRコード画面へ遷移
   *
   * @param modelAndView
   * @param principal
   * @param request
   * @return
   */
  @GetMapping("/friend/myqrcode")
  public ModelAndView directMyQrCodePage(ModelAndView modelAndView, @AuthenticationPrincipal final OidcUser principal,
      HttpServletRequest request) {

    String url = friendService.getFriendRequestLink(request, userService.getUserId(principal));
    System.out.println(url);

    modelAndView.setViewName("myqrcode");
    modelAndView.addObject("url", url);

    return modelAndView;
  }

  /**
   * フレンド承認画面へ遷移
   *
   * @param modelAndView
   * @param principal
   * @return
   */
  @GetMapping("/friend/acceptlist")
  public ModelAndView directFriendAcceptPage(ModelAndView modelAndView,
      @AuthenticationPrincipal final OidcUser principal) {

    List<ViewFriendRelationList> viewFriendRelationList = friendService
        .getFriendAcceptList(userService.getUserId(principal));

    modelAndView.setViewName("friendaccept");
    modelAndView.addObject("viewFriendRelationList", viewFriendRelationList);

    return modelAndView;
  }

  /**
   * フレンド申請画面へ遷移
   *
   * @param principal
   * @param encodedPath エンコードされたパス
   */
  @GetMapping("/friend/request/{encodedPath}")
  public ModelAndView directFriendRequestPage(ModelAndView modelAndview,
      @AuthenticationPrincipal final OidcUser principal, @PathVariable String encodedPath) {

    ViewFriendRelationList viewFriendRelationList = friendService.getNYRequestRelation(userService.getUserId(principal),
        encodedPath);

    modelAndview.setViewName("friendrequest");
    modelAndview.addObject("tfrId", viewFriendRelationList.getTfrId());
    modelAndview.addObject("acceptanceUserName", viewFriendRelationList.getAcceptanceUserName());

    return modelAndview;
  }

  /**
   * フレンドリクエストをする
   *
   * @param principal
   * @param tfrId     フレンドリレーションID
   */
  @ResponseBody
  @PostMapping("/friend/request")
  public void requestFriend(@AuthenticationPrincipal final OidcUser principal, @RequestParam("tfrId") long tfrId) {

    friendService.requestFriend(tfrId, userService.getUserId(principal));

  }

  /**
   * フレンドリクエストを承認する
   *
   * @param principal
   * @param tfrId     フレンドリレーションID
   */
  @ResponseBody
  @PostMapping("/friend/accept")
  public void acceptFriend(@AuthenticationPrincipal final OidcUser principal, @RequestParam("tfrId") long tfrId) {

    friendService.replyFriendRequest(tfrId, userService.getUserId(principal),
        kashiteConst.FRIEND_RELATION_STATUS_ACCEPTED);
  }

  /**
   * フレンドリクエストを拒否する
   *
   * @param principal
   * @param tfrId     フレンドリレーションID
   */
  @ResponseBody
  @PostMapping("/friend/refuse")
  public void refuseFriend(@AuthenticationPrincipal final OidcUser principal, @RequestParam("tfrId") long tfrId) {

    friendService.replyFriendRequest(tfrId, userService.getUserId(principal),
        kashiteConst.FRIEND_RELATION_STATUS_REFUSE);

  }

}
