# 워크플로우

## 리뷰 컨텍스트

### 리뷰 작성
```
CreateReviewCommand
├── memberId: MemberId
├── book: Book
└── content: string

CreateReviewResult = ReviewCreated | CreateReviewError

ReviewCreated
├── reviewId: ReviewId
└── createdAt: DateTime

CreateReviewError
├── MemberNotFound
└── InvalidContent

createReview: CreateReviewCommand → CreateReviewResult
```

### 리뷰 수정
```
UpdateReviewCommand
├── reviewId: ReviewId
├── memberId: MemberId
└── content: string

UpdateReviewResult = ReviewUpdated | UpdateReviewError

ReviewUpdated
├── reviewId: ReviewId
└── updatedAt: DateTime

UpdateReviewError
├── ReviewNotFound
├── NotReviewOwner
└── InvalidContent

updateReview: UpdateReviewCommand → UpdateReviewResult
```

### 리뷰 삭제
```
DeleteReviewCommand
├── reviewId: ReviewId
└── memberId: MemberId

DeleteReviewResult = ReviewDeleted | DeleteReviewError

ReviewDeleted
└── reviewId: ReviewId

DeleteReviewError
├── ReviewNotFound
└── NotReviewOwner

deleteReview: DeleteReviewCommand → DeleteReviewResult
```

### 좋아요 누르기
```
LikeReviewCommand
├── reviewId: ReviewId
└── memberId: MemberId

LikeReviewResult = ReviewLiked | LikeReviewError

ReviewLiked
├── likeId: LikeId
└── createdAt: DateTime

LikeReviewError
├── ReviewNotFound
├── MemberNotFound
└── AlreadyLiked

likeReview: LikeReviewCommand → LikeReviewResult
```

### 좋아요 취소
```
UnlikeReviewCommand
├── reviewId: ReviewId
└── memberId: MemberId

UnlikeReviewResult = ReviewUnliked | UnlikeReviewError

ReviewUnliked
└── likeId: LikeId

UnlikeReviewError
├── ReviewNotFound
└── LikeNotFound

unlikeReview: UnlikeReviewCommand → UnlikeReviewResult
```

### 댓글 작성
```
CreateCommentCommand
├── reviewId: ReviewId
├── memberId: MemberId
└── content: string

CreateCommentResult = CommentCreated | CreateCommentError

CommentCreated
├── commentId: CommentId
└── createdAt: DateTime

CreateCommentError
├── ReviewNotFound
├── MemberNotFound
└── InvalidContent

createComment: CreateCommentCommand → CreateCommentResult
```

### 댓글 삭제
```
DeleteCommentCommand
├── commentId: CommentId
└── memberId: MemberId

DeleteCommentResult = CommentDeleted | DeleteCommentError

CommentDeleted
├── commentId: CommentId
└── deletedAt: DateTime

DeleteCommentError
├── CommentNotFound
└── NotCommentOwner

deleteComment: DeleteCommentCommand → DeleteCommentResult
```

## 회원 컨텍스트

### 로그인
```
LoginCommand
├── email: string
└── password: string

LoginResult = LoggedIn | LoginError

LoggedIn
├── memberId: MemberId
└── token: string

LoginError
├── MemberNotFound
└── InvalidPassword

login: LoginCommand → LoginResult
```

### 로그아웃
```
LogoutCommand
└── memberId: MemberId

LogoutResult = LoggedOut | LogoutError

LoggedOut
└── memberId: MemberId

LogoutError
└── NotLoggedIn

logout: LogoutCommand → LogoutResult
```

### 이름 변경
```
ChangeNameCommand
├── memberId: MemberId
└── newName: string

ChangeNameResult = NameChanged | ChangeNameError

NameChanged
├── memberId: MemberId
└── updatedAt: DateTime

ChangeNameError
├── MemberNotFound
└── InvalidName

changeName: ChangeNameCommand → ChangeNameResult
```

## 고객지원 컨텍스트

### 공지사항 등록
```
CreateNoticeCommand
├── adminId: AdminId
├── title: string
└── content: string

CreateNoticeResult = NoticeCreated | CreateNoticeError

NoticeCreated
├── noticeId: NoticeId
└── createdAt: DateTime

CreateNoticeError
├── AdminNotFound
├── InvalidTitle
└── InvalidContent

createNotice: CreateNoticeCommand → CreateNoticeResult
```

### 공지사항 수정
```
UpdateNoticeCommand
├── noticeId: NoticeId
├── adminId: AdminId
├── title: string
└── content: string

UpdateNoticeResult = NoticeUpdated | UpdateNoticeError

NoticeUpdated
├── noticeId: NoticeId
└── updatedAt: DateTime

UpdateNoticeError
├── NoticeNotFound
├── NotNoticeOwner
├── InvalidTitle
└── InvalidContent

updateNotice: UpdateNoticeCommand → UpdateNoticeResult
```

### 공지사항 삭제
```
DeleteNoticeCommand
├── noticeId: NoticeId
└── adminId: AdminId

DeleteNoticeResult = NoticeDeleted | DeleteNoticeError

NoticeDeleted
└── noticeId: NoticeId

DeleteNoticeError
├── NoticeNotFound
└── NotNoticeOwner

deleteNotice: DeleteNoticeCommand → DeleteNoticeResult
```

### 문의 등록 (회원)
```
CreateMemberInquiryCommand
├── memberId: MemberId
├── title: string
└── content: string

CreateMemberInquiryResult = MemberInquiryCreated | CreateMemberInquiryError

MemberInquiryCreated
├── inquiryId: InquiryId
└── createdAt: DateTime

CreateMemberInquiryError
├── MemberNotFound
├── InvalidTitle
└── InvalidContent

createMemberInquiry: CreateMemberInquiryCommand → CreateMemberInquiryResult
```

### 문의 등록 (비회원)
```
CreateGuestInquiryCommand
├── guestContact: string
├── title: string
└── content: string

CreateGuestInquiryResult = GuestInquiryCreated | CreateGuestInquiryError

GuestInquiryCreated
├── inquiryId: InquiryId
└── createdAt: DateTime

CreateGuestInquiryError
├── InvalidContact
├── InvalidTitle
└── InvalidContent

createGuestInquiry: CreateGuestInquiryCommand → CreateGuestInquiryResult
```
