package kr.co.movieyouhwan.user.movie.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import kr.co.movieyouhwan.admin.cinema.service.AdminCinemaService;
import kr.co.movieyouhwan.admin.movie.domain.Movie;
import kr.co.movieyouhwan.admin.movie.domain.MovieDay;
import kr.co.movieyouhwan.admin.movie.domain.MovieImg;
import kr.co.movieyouhwan.admin.movie.domain.MovieVideo;
import kr.co.movieyouhwan.admin.movie.service.AdminMovieService;
import kr.co.movieyouhwan.admin.theater.domain.Theater;
import kr.co.movieyouhwan.admin.theater.service.AdminTheaterService;
import kr.co.movieyouhwan.common.page.PageInfo;
import kr.co.movieyouhwan.user.cinema.domain.Cinema;
import kr.co.movieyouhwan.user.cinema.domain.CinemaMovie;
import kr.co.movieyouhwan.user.member.domain.Member;
import kr.co.movieyouhwan.user.member.service.UserMemberService;
import kr.co.movieyouhwan.user.movie.domain.MovieList;
import kr.co.movieyouhwan.user.movie.domain.MovieReview;
import kr.co.movieyouhwan.user.movie.domain.MovieTicket;
import kr.co.movieyouhwan.user.movie.service.UserMovieService;
import kr.co.movieyouhwan.user.myPage.service.UserMyService;

@Controller
public class UserMovieController {
	@Autowired
	private AdminMovieService aMovieService;
	@Autowired
	private AdminCinemaService aCinemaService;
	@Autowired
	private AdminTheaterService aTheaterService;
	@Autowired
	private UserMovieService uMovieService;
	@Autowired
	private UserMyService uMyService;
	
	/**
	 * 현재 상영 영화 목록 화면
	 * @param mv
	 * @return
	 */
	@RequestMapping(value="/movieList.yh", method=RequestMethod.GET)
	public ModelAndView userMovieListNowView(
			HttpServletRequest request,
			ModelAndView mv,
			@RequestParam(value="currentPage", required=false) Integer currentPage) {
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("loginUser");
		
		// 페이징 처리
		int page = (currentPage != null ? currentPage : 1);
		PageInfo pageInfo = new PageInfo(page, uMovieService.printNowMovieCount(), 12, 5);
		// 현재 상영 영화 리스트 가져오기
		List<MovieList> mlList = uMovieService.printAllMovieNow();
		if(member!=null) {
			String memberId=member.getMemberId();
			List<Integer> myZzimMovieList = uMovieService.printMyZzimMovieList(memberId);
			if(myZzimMovieList == null) {
				System.out.println("null");
			}else {
				System.out.println(myZzimMovieList.toString());
			}
			
			for(int i = 0 ; i < mlList.size(); i++) {
				if(myZzimMovieList.contains(mlList.get(i).getMovieNo())) {
					mlList.get(i).setZzimYn("Y");
				}else {
					mlList.get(i).setZzimYn("N");
				}
			}
		}
		// 화면 출력
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("mlList", mlList);
		mv.setViewName("user/movie/movieListNow");
		return mv;
	}

	/**
	 * 상영 예정 영화 목록 화면
	 * @param mv
	 * @return
	 */
	@RequestMapping(value="/movieListAfter.yh", method=RequestMethod.GET)
	public ModelAndView userMovieListAfterView(
			HttpServletRequest request,
			ModelAndView mv,
			@RequestParam(value="currentPage", required=false) Integer currentPage) {
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("loginUser");
		// 페이징 처리
		int page = (currentPage != null ? currentPage : 1);
		PageInfo pageInfo = new PageInfo(page, uMovieService.printAfterMovieCount(), 12, 5);
		// 상영 예정 영화 리스트 가져오기
		List<MovieList> mlList = uMovieService.printAllMovieAfter();
		if(member!=null) {
			String memberId=member.getMemberId();
			List<Integer> myZzimMovieList = uMovieService.printMyZzimMovieList(memberId);
			if(myZzimMovieList == null) {
				System.out.println("null");
			}else {
				System.out.println(myZzimMovieList.toString());
			}
			
			for(int i = 0 ; i < mlList.size(); i++) {
				if(myZzimMovieList.contains(mlList.get(i).getMovieNo())) {
					mlList.get(i).setZzimYn("Y");
				}else {
					mlList.get(i).setZzimYn("N");
				}
			}
		}
		// 화면 출력
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("mlList", mlList);
		mv.setViewName("user/movie/movieListAfter");
		return mv;
	}
	
	/**
	 * 상영 종료 영화 목록 화면
	 * @param mv
	 * @return
	 */
	@RequestMapping(value="/movieListBefore.yh", method=RequestMethod.GET)
	public ModelAndView userMovieListBeforeView(
			HttpServletRequest request,
			ModelAndView mv,
			@RequestParam(value="currentPage", required=false) Integer currentPage) {
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("loginUser");
		// 페이징 처리
		int page = (currentPage != null ? currentPage : 1);
		PageInfo pageInfo = new PageInfo(page, uMovieService.printBeforeMovieCount(), 12, 5);
		// 상영 종료 영화 목록 리스트 가져오기
		List<MovieList> mlList = uMovieService.printAllMovieBefore();
		if(member!=null) {
			String memberId=member.getMemberId();
			List<Integer> myZzimMovieList = uMovieService.printMyZzimMovieList(memberId);
			if(myZzimMovieList == null) {
				System.out.println("null");
			}else {
				System.out.println(myZzimMovieList.toString());
			}
			
			for(int i = 0 ; i < mlList.size(); i++) {
				if(myZzimMovieList.contains(mlList.get(i).getMovieNo())) {
					mlList.get(i).setZzimYn("Y");
				}else {
					mlList.get(i).setZzimYn("N");
				}
			}
		}
		// 화면 출력
		mv.addObject("pageInfo", pageInfo);
		mv.addObject("mlList", mlList);
		mv.setViewName("user/movie/movieListBefore");
		return mv;
	}
	
	/**
	 * 영화 검색 완료 리스트
	 * @param mv
	 * @return
	 */
	@RequestMapping(value="/movieSearchList.yh", method=RequestMethod.GET)
	public ModelAndView userMovieSearchListView(
			ModelAndView mv,
			@RequestParam("searchName") String searchName) {
		// 영화 검색 완료 리스트 불러오기
		List<MovieList> mlList = uMovieService.printSearchMovie(searchName);
		// 화면출력
		if(!mlList.isEmpty()) {
			mv.addObject("mlList", mlList);
		}else {
			mv.addObject("mlList", null);
		}
		mv.addObject("searchName", searchName);
		mv.setViewName("user/movie/movieSearchList");
		return mv;
	}
	
	/**
	 * 영화 검색 기능
	 * @param mv
	 * @param searchName
	 * @return
	 */
	@RequestMapping(value="/movieListSearch.yh", method=RequestMethod.POST)
	public ModelAndView userMovieSearchList(
			ModelAndView mv,
			@ModelAttribute Movie movie,
			@RequestParam("searchName") String searchName) {
		// searchValue가 null이면 "" 로 처리
 		// String search = (searchValue != null ? searchValue : "");
		// 영화 검색 완료 리스트 불러오기
		List<MovieList> mlList = uMovieService.printSearchMovie(searchName);
		// 화면출력
		if(!mlList.isEmpty()) {
			mv.addObject("mlList", mlList);
		}else {
			mv.addObject("mlList", null);
		}
		mv.addObject("searchName", searchName);
		mv.setViewName("redirect:/movieSearchList.yh");
		return mv;
	}
	
	/**
	 * 영화 상세 페이지
	 * @param mv
	 * @param movieNo
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/movieDetail.yh", method=RequestMethod.GET)
	public ModelAndView userMovieDetailView(
			HttpServletRequest request,
			ModelAndView mv,
			@RequestParam("movieNo") Integer movieNo) {
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("loginUser");
		Movie movie = aMovieService.printOneMovie(movieNo);
		List<MovieImg> miList = aMovieService.printAllMovieImg(movieNo);
		List<MovieVideo> mvList = aMovieService.printAllMovieVideo(movieNo);
		// 나의 찜 목록 추가 예정
		mv.addObject("movie", movie);
		mv.addObject("miList", miList);
		mv.addObject("mvList", mvList);
		mv.setViewName("user/movie/movieDetail");
		return mv;
	}
	
	/**
	 * 리뷰 리스트 불러오기
	 * @param mv
	 * @param movieNo
	 * @return
	 */
	@RequestMapping(value="/movieReview.yh", method=RequestMethod.GET)
	public ModelAndView movieReview(
			HttpServletRequest request,
			ModelAndView mv,
			@RequestParam("movieNo") Integer movieNo) {
		HttpSession session=request.getSession();
		Member member=(Member)session.getAttribute("loginUser");
		if(member!=null) {
			MovieReview myMovieReview=uMovieService.printOneMovieReview(member.getMemberId(), movieNo);
			if(myMovieReview!=null) {
				mv.addObject("myMovieReview", myMovieReview);
			}
		}
		List<MovieReview> movieReviewList=uMovieService.printMovieReview(movieNo);
		Movie movie = aMovieService.printOneMovie(movieNo);
		List<MovieImg> miList = aMovieService.printAllMovieImg(movieNo);
		List<MovieVideo> mvList = aMovieService.printAllMovieVideo(movieNo);
		// 나의 찜 목록 추가 예정
		mv.addObject("movieReviewList", movieReviewList);
		mv.addObject("movie", movie);
		mv.addObject("miList", miList);
		mv.addObject("mvList", mvList);
		if(movieReviewList.size()!=0) {
			int movieRateSum=0;
			for(int i=0; i<movieReviewList.size(); i++) {
				movieRateSum+=movieReviewList.get(i).getMovieRate();
			}
			String movieTotalRate=String.format("%.1f",(double)movieRateSum/movieReviewList.size());
			mv.addObject("movieTotalRate", movieTotalRate);
		}else {
			mv.addObject("movieTotalRate", "-");
		}
		mv.setViewName("user/movie/movieReview");
		return mv;
	}
	
	@ResponseBody
	@RequestMapping(value="/movieReview.register.yh", method=RequestMethod.POST)
	public String registerMovieReview(HttpServletRequest request,
			@RequestParam("movieReview") String movieReview,
			@RequestParam("movieRate") Integer movieRate,
			@RequestParam("movieNo") Integer movieNo) {
		HttpSession session=request.getSession();
		Member member=(Member)session.getAttribute("loginUser");
		if(member==null) {
			return "loginRequired";
		}
		else {
			int isReviewAlreadyRegistered=uMovieService.checkReviewExist(member.getMemberId(), movieNo);
			if(isReviewAlreadyRegistered==0) {
				MovieReview review=new MovieReview();
				review.setMemberId(member.getMemberId());
				review.setMovieNo(movieNo);
				review.setMovieRate(movieRate);
				review.setMovieReview(movieReview);
				int result=uMovieService.registerMovieReview(review);
				if(result>0) {
					return "success";
				}
				else {
					return "123";
				}
			}
			else {
				return "fail";
			}
		}
	}
	
	@RequestMapping(value="/movieReview/delete.yh", method=RequestMethod.POST)
	public String deleteReview(HttpServletRequest request,
			@RequestParam("movieNo") Integer movieNo) {
		try {
			HttpSession session=request.getSession();
			Member member=(Member)session.getAttribute("loginUser");
			if(movieNo!=null && member!=null) {
				int result=uMovieService.deleteReview(movieNo, member.getMemberId());
				if(result>0) {
					return "redirect:/movieReview.yh?movieNo="+movieNo;
				}
			}
			else if(member==null) {
				return "redirect:/member/loginView.yh";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/movieReview/modify.yh", method=RequestMethod.POST)
	public String modifyReview(HttpServletRequest request,
			@ModelAttribute MovieReview movieReview) {
		HttpSession session=request.getSession();
		Member member=(Member)session.getAttribute("loginUser");
		if(movieReview!=null && member!=null) {
			movieReview.setMemberId(member.getMemberId());
			int result=uMovieService.modifyReview(movieReview);
			if(result>0) {
				return "redirect:/movieReview.yh?movieNo="+movieReview.getMovieNo();
			}
		}else {
			return "redirect:/member/loginView.yh";
		}
		return "redirect:/movieReview.yh?movieNo="+movieReview.getMovieNo();
	}
	
	/**
	 * 영화 예매 (영화관, 영화, 시간 선택)
	 * @param mv
	 * @param cinema
	 * @param theater
	 * @param movie
	 * @param movieTime
	 * @return
	 */
	@RequestMapping(value="/movieTicketTime.yh", method=RequestMethod.GET)
	public ModelAndView movieTicketTimeView(
			ModelAndView mv,
			HttpServletRequest request,
			@RequestParam(value="cinemaNo", required=false) Integer cinemaNo) {
		cinemaNo = cinemaNo == null ? 13 : cinemaNo;
		List<Cinema> cList = aCinemaService.printAllCinema();
		List<Movie> mList = uMovieService.printAllMovieCinema(cinemaNo);
		mv.addObject("cList", cList);
		mv.addObject("mList", mList);
		mv.addObject("movieDay", new MovieDay());
		mv.setViewName("user/movie/movieTicketTime");
		return mv;
	}
	
	/**
	 * 영화 예매 - 영화관, 영화 선택 AJAX
	 * @param cinemaNo
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="/movieTicketTimeCinema.yh", produces="application/json;charset=utf-8", method=RequestMethod.POST)
	public String movieTicketTimeCinemaChoice(
			@RequestParam(value="cinemaNo", required=false) Integer cinemaNo) {
		cinemaNo = cinemaNo == null ? 13 : cinemaNo;
		// 영화관별 상영 영화 출력
		List<Movie> mList = uMovieService.printAllMovieCinema(cinemaNo);
		Gson gson = new Gson();
		JSONObject object = new JSONObject();
		object.put("mList", gson.toJson(mList));
		return object.toJSONString();
	}
	
	/**
	 * 영화 예매 - 영화관, 영화, 상영 영화 선택 AJAX
	 * @param movie
	 * @param cinemaNo
	 * @param movieNo
	 * @param movieTitle
	 * @param movieDay
	 * @param dayIndex
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="/movieTicketTimeMovie.yh", produces="application/json;charset=utf-8", method=RequestMethod.POST)
	public String movieTicketTimeChoice(
			@ModelAttribute Movie movie,
			@RequestParam(value="cinemaNo", required=false) Integer cinemaNo,
			@RequestParam(value="movieNo", required=false) Integer movieNo,
			@RequestParam(value="movieTitle", required=false) String movieTitle,
			@RequestParam(value="movieDay", required=false) String movieDay,
			@RequestParam(value="dayIndex", required=false) Integer dayIndex,
			HttpSession session) {
		dayIndex = dayIndex == null ? 0 : dayIndex;
		// 영화관 이름 출력
		Cinema cinema = aCinemaService.printOneCinema(cinemaNo);
		// 영화관, 일별 영화 출력 (중복 제외)
		List<Movie> mList = uMovieService.printTicketMovieOne(cinemaNo, movieNo, new MovieDay().getMovieDay(dayIndex));
		// 영화관, 영화, 일별 상영 영화 출력 (중복 포함)
		List<CinemaMovie> cmList = uMovieService.printTicketMovieByDay(cinemaNo, movieNo, new MovieDay().getMovieDay(dayIndex));
		// 영화별 영화 이미지 출력
		List<MovieImg> miList = aMovieService.printAllMovieImg(movieNo);
		Gson gson = new Gson();
		JSONObject object = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		object.put("cinema", gson.toJson(cinema));
		object.put("mList", gson.toJson(mList));
		object.put("cmList", gson.toJson(cmList));
		object.put("miList", gson.toJson(miList));
		jsonArray.add(object);
		return jsonArray.toJSONString();
	}
	
	/**
	 * 상영 좌석 선택
	 * @param mv
	 * @param cinemaNo
	 * @param movieNo
	 * @param theaterNo
	 * @param movieImgRename
	 * @param cinemaName
	 * @param movieTitle
	 * @param movieDay
	 * @param movieStart
	 * @param movieEnd
	 * @param theaterName
	 * @param movieTicket
	 * @param movieSeat
	 * @return
	 */
	@RequestMapping(value="/movieTicketSeat.yh", method=RequestMethod.POST)
	public ModelAndView movieTicketSeat(
			ModelAndView mv,
			@ModelAttribute Movie movie,
			@RequestParam("cinemaNo") Integer cinemaNo,
			@RequestParam("movieNo") Integer movieNo,
			@RequestParam("theaterNo") Integer theaterNo,
			@RequestParam("movieImgRename") String movieImgRename,
			@RequestParam("cinemaName") String cinemaName,
			@RequestParam("movieTitle") String movieTitle,
			@RequestParam("movieDay") String movieDay,
			@RequestParam("movieStart") String movieStart,
			@RequestParam("movieEnd") String movieEnd,
			@RequestParam("theaterName") String theaterName,
			@RequestParam("movieTicket") Integer movieTicket,
			@RequestParam("movieSeat") Integer movieSeat,
			HttpServletRequest request,
			HttpSession session) {
		// 상영관 정보 가져오기
		Theater theater = aTheaterService.printOneTheater(theaterNo);
		// 알파벳 리스트
		List<String> aToZ = new ArrayList<String>();
		for(int i = 65; i < 91; i++) {
		  aToZ.add(String.valueOf((char)i));
		}
		// 화면 출력
		mv.addObject("cinemaNo", cinemaNo);
		mv.addObject("movieNo", movieNo);
		mv.addObject("theaterNo", theaterNo);
		mv.addObject("movieImgRename", movieImgRename);
		mv.addObject("cinemaName", cinemaName);
		mv.addObject("movieTitle", movieTitle);
		mv.addObject("movieDay", movieDay);
		mv.addObject("movieStart", movieStart);
		mv.addObject("movieEnd", movieEnd);
		mv.addObject("theaterName", theaterName);
		mv.addObject("movieTicket", movieTicket);
		mv.addObject("movieSeat", movieSeat);
		mv.addObject("theater", theater);
		mv.addObject("abcd", aToZ);
		mv.setViewName("user/movie/movieTicketSeat");
		return mv;
	}
	
	/**
	 * 영화 예매 결제 화면
	 * @param mv
	 * @param request
	 * @param cinemaNo
	 * @param movieNo
	 * @param theaterNo
	 * @param movieImgRename
	 * @param cinemaName
	 * @param movieTitle
	 * @param movieDay
	 * @param movieStart
	 * @param movieEnd
	 * @param theaterName
	 * @param movieTicket
	 * @param movieSeat
	 * @param seatChoice
	 * @param adultCount
	 * @param teenagerCount
	 * @param adultPay
	 * @param teenagerPay
	 * @return
	 */
	@RequestMapping(value="/movieTicketPay.yh", method=RequestMethod.POST)
	public ModelAndView movieTicketPay(
			ModelAndView mv,
			HttpServletRequest request,
			@RequestParam("cinemaNo") Integer cinemaNo,
			@RequestParam("movieNo") Integer movieNo,
			@RequestParam("theaterNo") Integer theaterNo,
			@RequestParam("movieImgRename") String movieImgRename,
			@RequestParam("cinemaName") String cinemaName,
			@RequestParam("movieTitle") String movieTitle,
			@RequestParam("movieDay") String movieDay,
			@RequestParam("movieStart") String movieStart,
			@RequestParam("movieEnd") String movieEnd,
			@RequestParam("theaterName") String theaterName,
			@RequestParam("movieTicket") Integer movieTicket,
			@RequestParam("movieSeat") Integer movieSeat,
			@RequestParam("seatChoice") String seatChoice,
			@RequestParam("adultCount") Integer adultCount,
			@RequestParam("teenagerCount") Integer teenagerCount,
			@RequestParam("adultPay") Integer adultPay,
			@RequestParam("teenagerPay") Integer teenagerPay) {
		// 사용자 정보 가져오기
		HttpSession session = request.getSession();
		String memberId = ((Member)session.getAttribute("loginUser")).getMemberId();
		Member member = uMyService.printOneById(memberId);
		String userNick = member.getMemberNick();
		int userPoint = member.getMemberPoint();
		String userBirth = member.getMemberBirth();
		String userGender = member.getMemberGender();
		// 화면 출력
		mv.addObject("cinemaNo", cinemaNo);
		mv.addObject("movieNo", movieNo);
		mv.addObject("theaterNo", theaterNo);
		mv.addObject("movieImgRename", movieImgRename);
		mv.addObject("cinemaName", cinemaName);
		mv.addObject("movieTitle", movieTitle);
		mv.addObject("movieDay", movieDay);
		mv.addObject("movieStart", movieStart);
		mv.addObject("movieEnd", movieEnd);
		mv.addObject("theaterName", theaterName);
		mv.addObject("movieTicket", movieTicket);
		mv.addObject("movieSeat", movieSeat);
		mv.addObject("seatChoice", seatChoice);
		mv.addObject("userNick", userNick);
		mv.addObject("userPoint", userPoint);
		mv.addObject("userBirth", userBirth);
		mv.addObject("userGender", userGender);
		mv.addObject("adultCount", adultCount);
		mv.addObject("teenagerCount", teenagerCount);
		mv.addObject("adultPay", adultPay);
		mv.addObject("teenagerPay", teenagerPay);
		mv.setViewName("user/movie/movieTicketPay");
		return mv;
	}
	
	/**
	 * 영화 예매자 정보 가져오기
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/movie/pay/buyer.yh", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	public String moviePayBuyer(
			HttpServletRequest request) {
		JSONObject jsonObj = new JSONObject();
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("loginUser");
		Member buyerInfo = uMovieService.printBuyerInfo(member.getMemberId());
		jsonObj.put("memberName", buyerInfo.getMemberName());
		jsonObj.put("memberPhone", buyerInfo.getMemberPhone());
		jsonObj.put("memberEmail", buyerInfo.getMemberEmail());
		return jsonObj.toString();
	}
	
	/**
	 * 영화 결제, 예매 정보 DB저장
	 * @param request
	 * @param movieTicket
	 * @param orderNo
	 * @param cinemaNo
	 * @param movieNo
	 * @param theaterNo
	 * @param movieImgRename
	 * @param movieName
	 * @param cinemaName
	 * @param theaterName
	 * @param movieDay
	 * @param movieStart
	 * @param movieEnd
	 * @param movieBuy
	 * @param movieSeat
	 * @param seatChoice
	 * @param userPoint
	 * @param adultPay
	 * @param teenagerPay
	 * @param payMethod
	 * @param payAmount
	 * @param paid_at
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/movie/pay.yh", method = RequestMethod.POST)
	public String moviePay(
			HttpServletRequest request,
			@ModelAttribute MovieTicket movieTicket,
			@RequestParam("orderNo") String orderNo,
			@RequestParam("cinemaNo") Integer cinemaNo,
			@RequestParam("movieNo") Integer movieNo,
			@RequestParam("theaterNo") Integer theaterNo,
			@RequestParam("movieImgRename") String movieImgRename,
			@RequestParam("movieName") String movieName,
			@RequestParam("cinemaName") String cinemaName,
			@RequestParam("theaterName") String theaterName,
			@RequestParam("movieDay") String movieDay,
			@RequestParam("movieStart") String movieStart,
			@RequestParam("movieEnd") String movieEnd,
			@RequestParam("movieTicket") Integer movieBuy,
			@RequestParam("movieSeat") Integer movieSeat,
			@RequestParam("seatChoice") String seatChoice,
			@RequestParam("userPoint") Integer userPoint,
			@RequestParam("adultPay") Integer adultPay,
			@RequestParam("teenagerPay") Integer teenagerPay,
			@RequestParam("payMethod") String payMethod,
			@RequestParam("payAmount") Integer payAmount,
			@RequestParam("paid_at") long paid_at,
			@RequestParam("status") String status) {
		// 주문자 정보 가져온 후 저장
		HttpSession session = request.getSession();
		Member member = (Member)session.getAttribute("loginUser");
		movieTicket.setMemberId(member.getMemberId());
		String memberId = member.getMemberId();
		String memberAge = member.getMemberBirth().substring(0,4);
		int today = Integer.parseInt("2022");
		int num = Integer.parseInt("1");
		int age = today - Integer.parseInt(memberAge) + num;
		movieTicket.setMemberAge(age);
		movieTicket.setMemberGender(member.getMemberGender());
		// 포인트 적립 금액
		int memberGrade = Integer.parseInt(member.getMemberLevel());
		if(memberGrade == 0) {
			int point = (int)(payAmount * 0.02);
			movieTicket.setAddPoint(point);
		}else if(memberGrade == 1) {
			int point = (int)(payAmount * 0.04);
			movieTicket.setAddPoint(point);
		}else if(memberGrade == 2) {
			int point = (int)(payAmount * 0.06);
			movieTicket.setAddPoint(point);
		}else if(memberGrade == 3) {
			int point = (int)(payAmount * 0.08);
			movieTicket.setAddPoint(point);
		}else if(memberGrade == 4) {
			int point = (int)(payAmount * 0.1);
			movieTicket.setAddPoint(point);
		}
		// 성인 예매자 수 구하기
		if(adultPay == 0) {
			movieTicket.setAdultCount(0);
		}else {
			int apay = (int)(adultPay / 15000);
			movieTicket.setAdultCount(apay);
		}
		// 청소년 예매자 수 구하기
		if(teenagerPay == 0) {
			movieTicket.setTeenagerCount(0);
		}else {
			int tpay = (int)(teenagerPay / 10000);
			movieTicket.setTeenagerCount(tpay);
		}
		// movieTicket 값 저장
		movieTicket.setTicketNo(orderNo);
		movieTicket.setCinemaNo(cinemaNo);
		movieTicket.setMovieNo(movieNo);
		movieTicket.setTheaterNo(theaterNo);
		movieTicket.setMovieName(movieName);
		movieTicket.setCinemaName(cinemaName);
		movieTicket.setTheaterName(theaterName);
		movieTicket.setMovieDay(movieDay);
		movieTicket.setMovieStart(movieStart);
		movieTicket.setPayMethod(payMethod);
		movieTicket.setChoiceSeat(seatChoice);
		movieTicket.setMoviePay(payAmount);
		movieTicket.setPayDate(new Date(paid_at));
		int result1 = uMovieService.registerMovieTicket(movieTicket);
		int result2 = uMovieService.modifyMemberPoint(memberId, userPoint);
		return "";
	}
	
	/**
	 * 영화 예매 완료 페이지
	 * @param mv
	 * @return
	 */
	@RequestMapping(value="/movieTicketComplete.yh", method=RequestMethod.GET)
	public ModelAndView movieComplete(
			ModelAndView mv) {
		mv.setViewName("user/movie/movieTicketComplete");
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value="/choiceTicketComplete.yh", produces = "application/json;charset=utf-8", method=RequestMethod.POST)
	public String ticketTimeMovieChoice(
			HttpServletRequest request,
			@RequestParam(value="cinemaNo", required=false) Integer cinemaNo,
			@RequestParam(value="movieNo", required=false) Integer movieNo,
			@RequestParam(value="movieDay", required=false) String movieDay) {
		List<MovieList> mlList = uMovieService.printTicketTimeChoice(cinemaNo, movieNo, movieDay);
		Gson gson = new Gson();
		JSONObject object = new JSONObject();
		object.put("mlList", gson.toJson(mlList));
		System.out.println(mlList.toString());
		return object.toJSONString();
	}
}