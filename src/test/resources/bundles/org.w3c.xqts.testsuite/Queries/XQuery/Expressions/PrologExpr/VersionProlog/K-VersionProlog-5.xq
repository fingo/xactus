(:*******************************************************:)
(: Test: K-VersionProlog-5                               :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:39+02:00                       :)
(: Purpose: A prolog containing many different declarations. :)
(:*******************************************************:)

		xquery version "1.0" encoding "ISO-8859";
		declare boundary-space preserve;
		declare default collation "http://www.w3.org/2005/xpath-functions/collation/codepoint";
		declare base-uri "http://example.com/";
		declare construction strip;
		declare ordering ordered;
		declare default order empty greatest;
		declare copy-namespaces no-preserve, no-inherit;
		declare namespace ex = "http://example.com/a/Namespace";
		declare default element namespace "http://example.com/";
		declare default function namespace "http://example.com/";
		declare option fn:x-notRecognized "option content";
		(: TODO function declarations missing :)
		(: TODO variable declarations missing :)
		1 eq 1
	