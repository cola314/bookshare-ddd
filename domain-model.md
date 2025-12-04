# 도메인 모델

## 리뷰 컨텍스트

### 리뷰 (Review) - 애그리거트 루트
```
├── id: ReviewId
├── memberId: MemberId (작성자)
├── book: Book (값 객체)
│   ├── url: string
│   ├── thumbnailUrl: string
│   ├── title: string
│   └── metadata: string
├── content: string
├── createdAt: DateTime
└── updatedAt: DateTime
```

### 좋아요 (Like) - 애그리거트 루트
```
├── id: LikeId
├── reviewId: ReviewId
├── memberId: MemberId
└── createdAt: DateTime
```

### 댓글 (Comment) - 애그리거트 루트
```
Comment = ActiveComment | DeletedComment

ActiveComment
├── id: CommentId
├── reviewId: ReviewId
├── memberId: MemberId
├── content: string
├── createdAt: DateTime
└── updatedAt: DateTime

DeletedComment
├── id: CommentId
├── reviewId: ReviewId
├── memberId: MemberId
└── deletedAt: DateTime
```

## 회원 컨텍스트

### 회원 (Member) - 애그리거트 루트
```
MemberId = RegularMemberId | AdminId

Member = RegularMember | AdminMember

RegularMember
├── id: RegularMemberId
├── name: string
├── email: string
├── createdAt: DateTime
└── updatedAt: DateTime

AdminMember
├── id: AdminId
├── name: string
├── email: string
├── createdAt: DateTime
└── updatedAt: DateTime
```

## 고객지원 컨텍스트

### 공지사항 (Notice) - 애그리거트 루트
```
├── id: NoticeId
├── adminId: MemberId
├── title: string
├── content: string
├── createdAt: DateTime
└── updatedAt: DateTime
```

### 문의 (Inquiry) - 애그리거트 루트
```
Inquiry = MemberInquiry | GuestInquiry

MemberInquiry
├── id: InquiryId
├── memberId: MemberId
├── title: string
├── content: string
└── createdAt: DateTime

GuestInquiry
├── id: InquiryId
├── guestContact: string
├── title: string
├── content: string
└── createdAt: DateTime
```
