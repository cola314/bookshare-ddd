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
├── id: CommentId
├── reviewId: ReviewId
├── memberId: MemberId
├── content: string
├── createdAt: DateTime
└── updatedAt: DateTime
```

## 회원 컨텍스트

### 회원 (Member) - 애그리거트 루트
```
├── id: MemberId
├── name: string
├── email: string
├── role: Role (회원/관리자)
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
├── id: InquiryId
├── memberId: MemberId? (nullable)
├── guestContact: string? (비회원 연락처)
├── title: string
├── content: string
└── createdAt: DateTime
```
