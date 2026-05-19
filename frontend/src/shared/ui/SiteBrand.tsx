import Image from 'next/image';

export const SITE_NAME = 'Nexus CRM';
export const SITE_LOGO_PATH = '/logo.png';

type SiteBrandProps = {
  variant?: 'inline' | 'stacked';
  logoSize?: number;
  className?: string;
};

export function SiteBrand({
  variant = 'inline',
  logoSize = 32,
  className = '',
}: SiteBrandProps) {
  const logo = (
    <Image
      src={SITE_LOGO_PATH}
      alt=""
      width={logoSize}
      height={logoSize}
      className="shrink-0 object-contain"
      priority
    />
  );

  const title = <span className="font-semibold text-primary">{SITE_NAME}</span>;

  if (variant === 'stacked') {
    return (
      <div className={`flex flex-col items-center gap-2 text-center ${className}`}>
        {logo}
        <h1 className="text-xl">{title}</h1>
      </div>
    );
  }

  return (
    <div className={`flex items-center gap-2 ${className}`}>
      {logo}
      <span className="text-lg">{title}</span>
    </div>
  );
}
